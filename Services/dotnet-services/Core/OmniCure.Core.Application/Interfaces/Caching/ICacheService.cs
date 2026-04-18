namespace OmniCure.Core.Application.Interfaces.Caching;

public interface ICacheService
{
    Task SetStringAsync(string key, string value, TimeSpan expiry, CancellationToken ct = default);
    Task SetAsync<T>(string key, T value, TimeSpan expiry, CancellationToken ct = default);
    Task<string?> GetStringAsync(string key, CancellationToken ct = default);
    Task<T?> GetAsync<T>(string key, CancellationToken ct = default);
    Task<T> GetOrSetAsync<T>(string key, Func<CancellationToken, Task<T>> factory, TimeSpan expiry, CancellationToken ct = default);
    Task RemoveAsync(string key, CancellationToken ct = default);
    Task RemoveByPrefixAsync(string prefix, CancellationToken ct = default);

    Task<bool> AcquireLockAsync(string lockKey, string lockValue, TimeSpan expiry, CancellationToken ct = default);
    Task<bool> ReleaseLockAsync(string lockKey, string lockValue, CancellationToken ct = default);

    Task PublishAsync(string channel, string message, CancellationToken ct = default);
    Task SubscribeAsync(string channel, Action<string> handler, CancellationToken ct = default);

    Task HashSetAsync(string key, Dictionary<string, string> fields, TimeSpan? expiry = null, CancellationToken ct = default);
    Task<Dictionary<string, string>> HashGetAllAsync(string key, CancellationToken ct = default);
    Task<string?> HashGetAsync(string key, string field, CancellationToken ct = default);
    Task HashDeleteAsync(string key, string field, CancellationToken ct = default);
}
