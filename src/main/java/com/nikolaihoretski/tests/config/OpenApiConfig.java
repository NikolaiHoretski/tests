package com.nikolaihoretski.tests.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String Default_Bearer_Auth = "BearerAuth";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(Default_Bearer_Auth))
                .components(new Components()
                        .addSecuritySchemes(Default_Bearer_Auth, createApiKeyScheme()));

    }

    private SecurityScheme createApiKeyScheme() {

        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

}
