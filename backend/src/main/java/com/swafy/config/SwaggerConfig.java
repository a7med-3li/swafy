package com.swafy.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    // TODO: configure OpenAPI / Swagger to use bearerAuth instead of basic-auth

    @Bean
    public OpenAPI customSwafyOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Swafy API")
                        .description("API documentation for Swafy backend")
                        .version("v1.0"))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .components(new Components()
                        .addSecuritySchemes("basicAuth",
                                new SecurityScheme()
                                        .name("basicAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                                        //.bearerFormat("JWT")
                        )
                );
    }

}
