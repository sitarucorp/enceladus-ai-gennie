package com.enceladus.ai.gennie.rag.config;

import com.enceladus.ai.gennie.rag.service.DocumentIngestionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadDocuments(
            DocumentIngestionService ingestionService) {
        return args -> {
            ingestionService.ingestCapitals();
            ingestionService.ingestBoot();
        };
    }
}
