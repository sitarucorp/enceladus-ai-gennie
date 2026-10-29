package com.enceladus.delivery.demo.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackendDocumentationConfig {
    @Value("${_DASHBOARD_API_VERSION:v1}")
    private String version;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Enceladus AI Gennie | Intelligence Console")
                        .title("Intelligence Console")
                        .version(version)
                        .summary("Enceladus AI | Gennie | Intelligence Console | Z2lyaXNoLm1haGFqYW5AbXBoYXNpcy5jb20K")
                        .description("Enceladus AI | Gennie | Intelligence Console for scanning blueprints & computer aided designs with audit review"));
    }
}
