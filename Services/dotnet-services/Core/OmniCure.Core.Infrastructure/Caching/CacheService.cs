using System.Text.Json;
using OmniCure.Core.Application.Interfaces.Caching;
using StackExchange.Redis;

namespace OmniCure.Core.Infrastructure.Caching;

public class CacheService(IConnectionMultiplexer redis) : ICacheService
{
    private readonly IDatabase _db = redis.GetDatabase();
    private static readonly JsonSerializerOptions JsonOptions = new()
    {
        PropertyNameCaseInsensitive = true
    };

    public async Task SetStringAsync(string key, string value, TimeSpan expiry, CancellationToken ct = default)
    {
        await _db.StringSetAsync(key, value, expiry);
    }

    public async Task SetAsync<T>(string key, T value, TimeSpan expiry, CancellationToken ct = default)
    {
        var json = JsonSerializer.Serialize(value, JsonOptions);
        await _db.StringSetAsync(key, json, expiry);
    }

    public async Task<string?> GetStringAsync(string key, CancellationToken ct = default)
    {
        var value = await _db.StringGetAsync(key);
        return value.HasValue ? value.ToString() : null;
    }

    public async Task<T?> GetAsync<T>(string key, CancellationToken ct = default)
    {
        var value = await _db.StringGetAsync(key);
        if (!value.HasValue) return default;
        return JsonSerializer.Deserialize<T>(value!, JsonOptions);
    }

    public async Task<T> GetOrSetAsync<T>(string key, Func<CancellationToken, Task<T>> factory, TimeSpan expiry, CancellationToken ct = default)
    {
        var cached = await GetAsync<T>(key, ct);
        if (cached is not null) return cached;

        var value = await factory(ct);
        await SetAsync(key, value, expiry, ct);
        return value;
    }

    public async Task RemoveAsync(string key, CancellationToken ct = default)
    {
        await _db.KeyDeleteAsync(key);
    }

    public async Task RemoveByPrefixAsync(string prefix, CancellationToken ct = default)
    {
        var endpoints = redis.GetEndPoints();
        foreach (var endpoint in endpoints)
        {
            var server = redis.GetServer(endpoint);
            await foreach (var key in server.KeysAsync(pattern: $"{prefix}*"))
            {
                await _db.KeyDeleteAsync(key);
            }
        }
    }

    public async Task<bool> AcquireLockAsync(string lockKey, string lockValue, TimeSpan expiry, CancellationToken ct = default)
    {
        return await _db.StringSetAsync(lockKey, lockValue, expiry, When.NotExists);
    }

    public async Task<bool> ReleaseLockAsync(string lockKey, string lockValue, CancellationToken ct = default)
    {
        const string luaScript = """
            if redis.call('get', KEYS[1]) == ARGV[1] then
                return redis.call('del', KEYS[1])
            else
                return 0
            end
            """;
        var result = await _db.ScriptEvaluateAsync(luaScript, [new RedisKey(lockKey)], [new RedisValue(lockValue)]);
        return (int)result == 1;
    }

    public async Task PublishAsync(string channel, string message, CancellationToken ct = default)
    {
        var subscriber = redis.GetSubscriber();
        await subscriber.PublishAsync(RedisChannel.Literal(channel), message);
    }

    public async Task SubscribeAsync(string channel, Action<string> handler, CancellationToken ct = default)
    {
        var subscriber = redis.GetSubscriber();
        await subscriber.SubscribeAsync(RedisChannel.Literal(channel), (_, value) =>
        {
            handler(value!);
        });
    }

    public async Task HashSetAsync(string key, Dictionary<string, string> fields, TimeSpan? expiry = null, CancellationToken ct = default)
    {
        var entries = fields.Select(f => new HashEntry(f.Key, f.Value)).ToArray();
        await _db.HashSetAsync(key, entries);
        if (expiry.HasValue)
            await _db.KeyExpireAsync(key, expiry.Value);
    }

    public async Task<Dictionary<string, string>> HashGetAllAsync(string key, CancellationToken ct = default)
    {
        var entries = await _db.HashGetAllAsync(key);
        return entries.ToDictionary(e => e.Name.ToString(), e => e.Value.ToString());
    }

    public async Task<string?> HashGetAsync(string key, string field, CancellationToken ct = default)
    {
        var value = await _db.HashGetAsync(key, field);
        return value.HasValue ? value.ToString() : null;
    }

    public async Task HashDeleteAsync(string key, string field, CancellationToken ct = default)
    {
        await _db.HashDeleteAsync(key, field);
    }
}
