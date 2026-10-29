package com.enceladus.ai.gennie.ingestions.service;


import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DocumentIngestionService {

    private final VectorStore vectorStore;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void ingestCapitals() {
        ingest("classpath:/documents/capitals.pdf");
    }

    public void ingestBoot() {
        ingest("classpath:/documents/spring-boot-guide.pdf");
    }

    private void ingest(String resourcePath) {
        System.out.println("Starting PDF ingestion: " + resourcePath);
        PagePdfDocumentReader pdfReader =
                new PagePdfDocumentReader(resourcePath);

        List<Document> documents = pdfReader.read();
        System.out.println("PDF loaded. Documents found: " + documents.size());

        TokenTextSplitter textSplitter =
                TokenTextSplitter.builder()
                        .withChunkSize(500)
                        .withMinChunkSizeChars(200)
                        .build();

        List<Document> chunks = textSplitter.split(documents);
        System.out.println("Documents split into chunks: " + chunks.size());

        List<Document> stableChunks = new ArrayList<>(chunks.size());
        for (int index = 0; index < chunks.size(); index++) {
            Document chunk = chunks.get(index);
            Map<String, Object> metadata = new HashMap<>(chunk.getMetadata());
            metadata.put("source", resourcePath);
            stableChunks.add(new Document(stableId(resourcePath, index), chunk.getText(), metadata));
        }

        // Stable IDs let PGVectorStore upsert the same PDF chunks on restarts.
        vectorStore.write(stableChunks);
        System.out.println("Documents successfully stored in PGVector!");
    }

    private String stableId(String resourcePath, int chunkIndex) {
        byte[] idBytes = (resourcePath + ":" + chunkIndex).getBytes(StandardCharsets.UTF_8);
        return UUID.nameUUIDFromBytes(idBytes).toString();
    }
}

