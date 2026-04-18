package ai.omnicure.iam.infrastructure.config;

import ai.omnicure.iam.application.feature.auth.cache.RefreshTokenCacheEntry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheEntryConfig {

    @Bean
    public RefreshTokenCacheEntry refreshTokenCacheEntry(RefreshTokenCacheSettings settings) {
        return new RefreshTokenCacheEntry(settings.getPrefixKey());
    }
}
