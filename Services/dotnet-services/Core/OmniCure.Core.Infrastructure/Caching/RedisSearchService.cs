using NRedisStack.RedisStackCommands;
using NRedisStack.Search;
using NRedisStack.Search.Literals.Enums;
using OmniCure.Core.Application.Interfaces.Caching;
using StackExchange.Redis;

namespace OmniCure.Core.Infrastructure.Caching;

public class RedisSearchService(IConnectionMultiplexer redis) : IRedisSearchService
{
    private readonly IDatabase _db = redis.GetDatabase();

    public async Task CreateIndexAsync(string indexName, string prefix, Action<ISchemaBuilder> schemaBuilder)
    {
        var builder = new RedisSchemaBuilder();
        schemaBuilder(builder);

        var options = new FTCreateParams()
            .On(IndexDataType.HASH)
            .Prefix(prefix);

        await _db.FT().CreateAsync(indexName, options, builder.Build());
    }

    public async Task DropIndexAsync(string indexName, bool deleteDocuments = false)
    {
        await _db.FT().DropIndexAsync(indexName, deleteDocuments);
    }

    public async Task<bool> IndexExistsAsync(string indexName)
    {
        try
        {
            await _db.FT().InfoAsync(indexName);
            return true;
        }
        catch (RedisServerException)
        {
            return false;
        }
    }

    public async Task ReindexAsync(string indexName, string prefix, Action<ISchemaBuilder> schemaBuilder)
    {
        if (await IndexExistsAsync(indexName))
            await DropIndexAsync(indexName);

        await CreateIndexAsync(indexName, prefix, schemaBuilder);
    }

    public async Task<SearchResponse> SearchAsync(string indexName, string query, int offset = 0, int limit = 10)
    {
        return await SearchAsync(indexName, query, null, true, offset, limit);
    }

    public async Task<SearchResponse> SearchAsync(string indexName, string query, string? sortBy = null, bool ascending = true, int offset = 0, int limit = 10, params string[] returnFields)
    {
        var q = new Query(query).Limit(offset, limit);

        if (sortBy is not null)
            q.SetSortBy(sortBy, ascending);

        if (returnFields.Length > 0)
            q.ReturnFields(returnFields);

        var result = await _db.FT().SearchAsync(indexName, q);

        var documents = result.Documents.Select(doc => new SearchDocument(
            doc.Id,
            doc.GetProperties()
                .ToDictionary(p => p.Key, p => p.Value.ToString())
        )).ToList();

        return new SearchResponse(result.TotalResults, documents);
    }

    public async Task SetHashAsync(string key, Dictionary<string, object> fields, TimeSpan? expiry = null)
    {
        var entries = fields.Select(f => new HashEntry(f.Key, f.Value.ToString() ?? "")).ToArray();
        await _db.HashSetAsync(key, entries);
        if (expiry.HasValue)
            await _db.KeyExpireAsync(key, expiry.Value);
    }

    public async Task DeleteHashAsync(string key)
    {
        await _db.KeyDeleteAsync(key);
    }
}

internal class RedisSchemaBuilder : ISchemaBuilder
{
    private readonly Schema _schema = new();

    public Schema Build() => _schema;

    public ISchemaBuilder AddTextField(string name, double weight = 1.0, bool sortable = false, bool noStem = false)
    {
        _schema.AddTextField(name, weight, sortable, noStem);
        return this;
    }

    public ISchemaBuilder AddTagField(string name, string separator = ",", bool sortable = false)
    {
        _schema.AddTagField(name, sortable, separator: separator);
        return this;
    }

    public ISchemaBuilder AddNumericField(string name, bool sortable = false)
    {
        _schema.AddNumericField(name, sortable);
        return this;
    }

    public ISchemaBuilder AddGeoField(string name)
    {
        _schema.AddGeoField(name);
        return this;
    }

    public ISchemaBuilder AddVectorField(string name, string algorithm, int dim, string distanceMetric)
    {
        var algo = algorithm.ToUpperInvariant() == "HNSW"
            ? Schema.VectorField.VectorAlgo.HNSW
            : Schema.VectorField.VectorAlgo.FLAT;

        _schema.AddVectorField(name, algo, new Dictionary<string, object>
        {
            { "TYPE", "FLOAT32" },
            { "DIM", dim },
            { "DISTANCE_METRIC", distanceMetric }
        });
        return this;
    }
}
