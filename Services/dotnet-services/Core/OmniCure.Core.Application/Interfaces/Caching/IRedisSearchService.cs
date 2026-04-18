namespace OmniCure.Core.Application.Interfaces.Caching;

public record SearchDocument(string Id, Dictionary<string, string> Fields);

public record SearchResponse(long TotalResults, List<SearchDocument> Documents);

public interface IRedisSearchService
{
    Task CreateIndexAsync(string indexName, string prefix, Action<ISchemaBuilder> schemaBuilder);
    Task DropIndexAsync(string indexName, bool deleteDocuments = false);
    Task<bool> IndexExistsAsync(string indexName);
    Task ReindexAsync(string indexName, string prefix, Action<ISchemaBuilder> schemaBuilder);

    Task<SearchResponse> SearchAsync(string indexName, string query, int offset = 0, int limit = 10);
    Task<SearchResponse> SearchAsync(string indexName, string query, string? sortBy = null, bool ascending = true, int offset = 0, int limit = 10, params string[] returnFields);

    Task SetHashAsync(string key, Dictionary<string, object> fields, TimeSpan? expiry = null);
    Task DeleteHashAsync(string key);
}

public interface ISchemaBuilder
{
    ISchemaBuilder AddTextField(string name, double weight = 1.0, bool sortable = false, bool noStem = false);
    ISchemaBuilder AddTagField(string name, string separator = ",", bool sortable = false);
    ISchemaBuilder AddNumericField(string name, bool sortable = false);
    ISchemaBuilder AddGeoField(string name);
    ISchemaBuilder AddVectorField(string name, string algorithm, int dim, string distanceMetric);
}
