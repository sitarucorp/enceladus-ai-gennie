package com.gsmahajan.testbed.springairag.service;


import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DocumentIngestionService {

    private final VectorStore vectorStore;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void ingestCapitals() {

        System.out.println("Starting PDF ingestion...");

        // 1. Read the PDF
        PagePdfDocumentReader pdfReader =
                new PagePdfDocumentReader(
                        "classpath:/documents/capitals.pdf"
                );

        List<Document> documents = pdfReader.read();

        System.out.println(
                "PDF loaded. Documents found: " + documents.size()
        );

        // 2. Split the documents into smaller chunks
        TokenTextSplitter textSplitter =
                TokenTextSplitter.builder()
                        .withChunkSize(500)
                        .withMinChunkSizeChars(200)
                        .build();

        List<Document> chunks =
                textSplitter.split(documents);

        System.out.println(
                "Documents split into chunks: " + chunks.size()
        );

        // 3. Generate embeddings and store them in PGVector
        vectorStore.write(chunks);

        System.out.println(
                "Documents successfully stored in PGVector!"
        );
    }
    public void ingestBoot() {

        System.out.println("Starting PDF ingestion...");

        // 1. Read the PDF
        PagePdfDocumentReader pdfReader =
                new PagePdfDocumentReader(
                        "classpath:/documents/spring-boot-guide.pdf"
                );

        List<Document> documents = pdfReader.read();

        System.out.println(
                "PDF loaded. Documents found: " + documents.size()
        );

        // 2. Split the documents into smaller chunks
        TokenTextSplitter textSplitter =
                TokenTextSplitter.builder()
                        .withChunkSize(500)
                        .withMinChunkSizeChars(200)
                        .build();

        List<Document> chunks =
                textSplitter.split(documents);

        System.out.println(
                "Documents split into chunks: " + chunks.size()
        );

        // 3. Generate embeddings and store them in PGVector
        vectorStore.write(chunks);

        System.out.println(
                "Documents successfully stored in PGVector!"
        );
    }
}

