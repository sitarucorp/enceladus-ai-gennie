package com.enceladus.ai.gennie.rag.config;

import com.enceladus.ai.gennie.rag.service.DocumentIngestionService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

class DataLoaderTest {
    @Test
    void loadsBothBundledDocumentsInOrder() throws Exception {
        DocumentIngestionService ingestionService = mock(DocumentIngestionService.class);
        CommandLineRunner runner = new DataLoader().loadDocuments(ingestionService);
        runner.run();
        var calls = inOrder(ingestionService);
        calls.verify(ingestionService).ingestCapitals();
        calls.verify(ingestionService).ingestBoot();
    }
}
