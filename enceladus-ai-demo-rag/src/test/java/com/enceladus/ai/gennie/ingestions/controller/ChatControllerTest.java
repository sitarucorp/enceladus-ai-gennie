package com.enceladus.ai.gennie.ingestions.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChatControllerTest {
    @Test
    void returnsChatCompletionForMessage() {
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        when(builder.build()).thenReturn(chatClient);
        when(chatClient.prompt().user(anyString()).call().content()).thenReturn("reply");
        assertEquals("reply", new ChatController(builder).chat("hello"));
    }
}
