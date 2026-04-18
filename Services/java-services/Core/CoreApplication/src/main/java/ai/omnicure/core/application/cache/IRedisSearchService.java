package ai.omnicure.core.application.cache;

import java.util.List;
import java.util.Map;

public interface IRedisSearchService {

    void createIndex(String indexName, String prefix, java.util.function.Consumer<SchemaBuilder> schemaBuilder);

    void dropIndex(String indexName, boolean deleteDocuments);

    boolean indexExists(String indexName);

    void reindex(String indexName, String prefix, java.util.function.Consumer<SchemaBuilder> schemaBuilder);

    SearchResponse search(String indexName, String query, int offset, int limit);

    SearchResponse search(String indexName, String query, String sortBy, boolean ascending, int offset, int limit, String... returnFields);

    void setHash(String key, Map<String, Object> fields, java.time.Duration expiry);

    void deleteHash(String key);

    record SearchDocument(String id, Map<String, String> fields) {}

    record SearchResponse(long totalResults, List<SearchDocument> documents) {}

    interface SchemaBuilder {
        SchemaBuilder addTextField(String name, double weight, boolean sortable, boolean noStem);
        SchemaBuilder addTagField(String name, String separator, boolean sortable);
        SchemaBuilder addNumericField(String name, boolean sortable);
        SchemaBuilder addGeoField(String name);
        SchemaBuilder addVectorField(String name, String algorithm, int dim, String distanceMetric);
    }
}
