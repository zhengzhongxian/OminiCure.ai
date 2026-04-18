package ai.omnicure.core.shared.constant;

public final class KeyConstants {

    private KeyConstants() {}

    public static final class ConnectionStrings {
        private ConnectionStrings() {}
        
        public static final String WRITE_DATA_SOURCE_PROPERTIES = "writeDataSourceProperties";
        public static final String WRITE_DATA_SOURCE = "writeDataSource";
        public static final String WRITE_ENTITY_MANAGER_FACTORY = "writeEntityManagerFactory";
        public static final String WRITE_TRANSACTION_MANAGER = "writeTransactionManager";

        public static final String READ_DATA_SOURCE_PROPERTIES = "readDataSourceProperties";
        public static final String READ_DATA_SOURCE = "readDataSource";
        public static final String READ_ENTITY_MANAGER_FACTORY = "readEntityManagerFactory";
        public static final String READ_TRANSACTION_MANAGER = "readTransactionManager";
    }

    public static final class ConfigurationSections {
        private ConfigurationSections() {}
        
        public static final String WRITE_DATASOURCE = "app.datasource.write";
        public static final String WRITE_HIKARI = "app.datasource.write.hikari";
        public static final String READ_DATASOURCE = "app.datasource.read";
        public static final String READ_HIKARI = "app.datasource.read.hikari";
        public static final String REDIS = "spring.data.redis";
        public static final String JWT_SETTINGS = "app.security.jwt";
        public static final String REDIS_REFRESH_TOKENS = "redis.refresh-tokens";
    }

    public static final class DatabaseInitializationSettings {
        private DatabaseInitializationSettings() {}
        public static final String MAX_RETRIES = "app.migration.max-retries";
        public static final String RETRY_INTERVAL = "app.migration.retry-interval";
    }
}
