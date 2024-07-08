package com.angelfg.best_travel.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Best Travel API",
        version = "1.0.0",
        description = "Documentation for endpoints in Best Travel"
    )
)
public class OpenApiConfig {
    // JSON de configuracion
    // http://localhost:8080/best_travel/v3/api-docs

    // Swagger endpoints
    // http://localhost:8080/best_travel/swagger-ui/index.html
}
