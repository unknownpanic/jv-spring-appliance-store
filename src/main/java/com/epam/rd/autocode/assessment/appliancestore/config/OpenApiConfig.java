package com.epam.rd.autocode.assessment.appliancestore.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    public static final String AUTHORIZATION_SCHEMA = "bearer";
    public static final String AUTHORIZATION_TOKEN_FORMAT = "JWT";
    public static final String SECURITY_SCHEMA = "BearerAuth";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(SECURITY_SCHEMA,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(AUTHORIZATION_SCHEMA)
                                .bearerFormat(AUTHORIZATION_TOKEN_FORMAT)))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEMA));
    }
}
