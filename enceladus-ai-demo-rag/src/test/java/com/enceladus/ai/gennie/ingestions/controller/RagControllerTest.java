package com.enceladus.ai.gennie.rag.controller;

import com.enceladus.ai.gennie.rag.model.RagResponse;
import com.enceladus.ai.gennie.rag.service.RagService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RagControllerTest {
    @Test
    void delegatesQuestionAndReturnsServiceResponse() {
        RagService service = mock(RagService.class);
        RagResponse expected = new RagResponse("question", "answer", List.of());
        when(service.ask("question")).thenReturn(expected);
        assertSame(expected, new RagController(service).ask("question"));
        verify(service).ask("question");
    }
}
