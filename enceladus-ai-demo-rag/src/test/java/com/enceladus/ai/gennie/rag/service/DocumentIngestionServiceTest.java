package com.enceladus.ai.gennie.rag.service;

import com.enceladus.ai.gennie.rag.config.DataLoader;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DocumentIngestionServiceTest {

    @Test
    void startupReusesStableChunkIdsForEachBundledPdf() throws Exception {
        VectorStore vectorStore = mock(VectorStore.class);
        doCallRealMethod().when(vectorStore).write(anyList());
        doCallRealMethod().when(vectorStore).accept(anyList());

        DocumentIngestionService ingestionService = new DocumentIngestionService(vectorStore);
        CommandLineRunner loadDocuments = new DataLoader().loadDocuments(ingestionService);

        loadDocuments.run();
        loadDocuments.run();

        ArgumentCaptor<List<Document>> chunksCaptor = ArgumentCaptor.forClass(List.class);
        verify(vectorStore, times(4)).add(chunksCaptor.capture());
        List<List<Document>> writes = chunksCaptor.getAllValues();

        assertEquals(ids(writes.get(0)), ids(writes.get(2)), "capital PDF IDs should remain stable");
        assertEquals(ids(writes.get(1)), ids(writes.get(3)), "Spring Boot PDF IDs should remain stable");
        assertNotEquals(ids(writes.get(0)), ids(writes.get(1)), "different PDFs must use different IDs");
        assertEquals("classpath:/documents/capitals.pdf", writes.get(0).getFirst().getMetadata().get("source"));
        assertEquals("classpath:/documents/spring-boot-guide.pdf", writes.get(1).getFirst().getMetadata().get("source"));
    }

    private List<String> ids(List<Document> documents) {
        return documents.stream().map(Document::getId).toList();
    }
}
