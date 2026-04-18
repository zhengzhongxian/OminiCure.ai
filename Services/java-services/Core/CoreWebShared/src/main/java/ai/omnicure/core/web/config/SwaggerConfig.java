package ai.omnicure.core.web.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name:OmniCure Service}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(new Info().title(applicationName + " API")
                        .description("OmniCure.ai Healthcare Platform API Documentation")
                        .version("v1.0.0")
                        .contact(new Contact().name("OmniCure Team").email("support@omnicure.ai"))
                        .license(new License().name("Proprietary").url("https://omnicure.ai")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description("Enter JWT Access Token here to authenticate. <br/>\nExp: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                                )
                );
    }
}
