package com.enceladus.ai.gennie.ingestions.service;

import com.enceladus.ai.gennie.ingestions.model.RagResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RagServiceTest {

    @Test
    void asksWithRetrievedContextAndReturnsCleanedSourceText() {
        VectorStore vectorStore = mock(VectorStore.class);
        ChatClient.Builder chatClientBuilder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        when(chatClientBuilder.build()).thenReturn(chatClient);
        when(chatClient.prompt().system(anyString()).user(anyString()).call().content()).thenReturn("The answer.");

        String longSource = "  Spring   Boot " + "x".repeat(520) + "  ";
        when(vectorStore.similaritySearch(any(SearchRequest.class)))
                .thenReturn(List.of(new Document(longSource)));

        RagResponse response = new RagService(vectorStore, chatClientBuilder).ask("What is Spring Boot?");

        assertEquals("What is Spring Boot?", response.question());
        assertEquals("The answer.", response.answer());
        assertEquals(1, response.sources().size());
        assertEquals(503, response.sources().getFirst().content().length());
        assertEquals("Spring Boot " + "x".repeat(488) + "...", response.sources().getFirst().content());
    }

    @Test
    @DisplayName("""
            Test to detect if the document stored in English but the RAG is questioned in different language, does it recognize and respond
            For example the capitals.pdf stores the data in country and capital city name in plain english but RAG is queried with lets say Marathi language ?
            """)
    @Disabled
    void asksWithOtherLanguageThanEnglishThenReturnsCleanedDetectedLanguageRespondIfDocumentStoredInEnglish() {
        VectorStore vectorStore = mock(VectorStore.class);
        ChatClient.Builder chatClientBuilder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        when(chatClientBuilder.build()).thenReturn(chatClient);
        when(chatClient.prompt().system(anyString()).user(anyString()).call().content()).thenReturn("The answer.");

        String longSource = "युनाईटेड किंग्डम च्या देशाची राजधानी लंडन आहे.";

        when(vectorStore.similaritySearch(any(SearchRequest.class)))
                .thenReturn(List.of(new Document(longSource)));

        RagResponse response = new RagService(vectorStore, chatClientBuilder).ask("UK देशाची राजधानी कोणती ?");

        assertEquals("UK देशाची राजधानी कोणती ?", response.question());
        assertEquals("The answer.", response.answer());
        assertEquals(1, response.sources().size());
        assertEquals(503, response.sources().getFirst().content().length());
        assertEquals("Spring Boot " + "x".repeat(488) + "...", response.sources().getFirst().content());
    }

    @Test
    void returnsNoSourcesWhenRetrievalFindsNoDocuments() {
        VectorStore vectorStore = mock(VectorStore.class);
        ChatClient.Builder chatClientBuilder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        when(chatClientBuilder.build()).thenReturn(chatClient);
        when(chatClient.prompt().system(anyString()).user(anyString()).call().content()).thenReturn("No context.");
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(List.of());

        RagResponse response = new RagService(vectorStore, chatClientBuilder).ask("Unknown question");

        assertEquals("No context.", response.answer());
        assertEquals(List.of(), response.sources());
    }

    @Test
    void returnsWhitespaceNormalizedSourceWhenItFitsWithinLimit() {
        VectorStore vectorStore = mock(VectorStore.class);
        ChatClient.Builder chatClientBuilder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        when(chatClientBuilder.build()).thenReturn(chatClient);
        when(chatClient.prompt().system(anyString()).user(anyString()).call().content()).thenReturn("Answer.");
        when(vectorStore.similaritySearch(any(SearchRequest.class)))
                .thenReturn(List.of(new Document("  short   source  ")));

        RagResponse response = new RagService(vectorStore, chatClientBuilder).ask("Question");

        assertEquals("short source", response.sources().getFirst().content());
    }
}
