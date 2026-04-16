package ai.omnicure.core.infra.config;

import ai.omnicure.core.shared.constant.KeyConstants;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    private static final Logger logger = LoggerFactory.getLogger(FlywayConfig.class);

    @Value("${" + KeyConstants.DatabaseInitializationSettings.MAX_RETRIES + ":5}")
    private int maxRetries;

    @Value("${" + KeyConstants.DatabaseInitializationSettings.RETRY_INTERVAL + ":5000}")
    private int retryIntervalTime;

    @Bean
    public FlywayMigrationStrategy customFlywayMigrationStrategy() {
        return flyway -> {
            int retryCount = 0;

            while (retryCount < maxRetries) {
                try {
                    logger.info("Applying database migrations... (Attempt {}/{})", retryCount + 1, maxRetries);
                    flyway.migrate();
                    logger.info("[Migration Progress: 100%] Database migrations applied successfully on WriteDB.");
                    break;
                } catch (Exception ex) {
                    retryCount++;
                    logger.warn("API: Database migration failed on attempt {}/{}. Retrying in {} ms... Error: {}",
                            retryCount, maxRetries, retryIntervalTime, ex.getMessage());
                    if (retryCount >= maxRetries) {
                        logger.error("API: Database migration failed after {} retries. Aborting.", maxRetries);
                        throw ex; // Re-throw to fail application startup
                    }
                    try {
                        Thread.sleep(retryIntervalTime);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException("Migration thread interrupted", ie);
                    }
                }
            }
        };
    }
}
