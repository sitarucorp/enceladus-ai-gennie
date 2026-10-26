package com.gsmahajan.testbed.springairag.config;

import com.gsmahajan.testbed.springairag.service.DocumentIngestionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadDocuments(
            DocumentIngestionService ingestionService) {
        ingestionService.ingestCapitals();
        return args -> ingestionService.ingestBoot();
    }
}