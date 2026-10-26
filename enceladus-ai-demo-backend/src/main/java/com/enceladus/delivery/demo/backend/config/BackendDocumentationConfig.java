package com.enceladus.delivery.demo.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackendDocumentationConfig {
    @Value("${_DASHBOARD_API_VERSION:unknown}")
    private String version;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Mphasis Demo Backend Application | Delivery Console")
                        .title("Delivery Console")
                        .version(version)
                        .summary("CSP dashboard | Delivery Console | M-Phasis | Z2lyaXNoLm1haGFqYW5AbXBoYXNpcy5jb20K")
                        .description("Java Spring Boot Microservices Backend with dashboard"));
    }
}
