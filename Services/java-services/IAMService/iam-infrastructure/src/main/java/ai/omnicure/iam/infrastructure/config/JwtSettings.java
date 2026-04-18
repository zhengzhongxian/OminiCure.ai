package ai.omnicure.iam.infrastructure.config;

import ai.omnicure.core.shared.constant.KeyConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = KeyConstants.ConfigurationSections.JWT_SETTINGS)
public class JwtSettings {
    private String key = "";
    private String issuer = "";
    private String audience = "";
    private int accessTokenDurationMinutes = 30;
    private int refreshTokenDurationDays = 7;
}
