package ai.omnicure.iam.infrastructure.config;

import ai.omnicure.core.shared.constant.KeyConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = KeyConstants.ConfigurationSections.REDIS_REFRESH_TOKENS)
public class RefreshTokenCacheSettings {
    private String prefixKey = "rt";
}
