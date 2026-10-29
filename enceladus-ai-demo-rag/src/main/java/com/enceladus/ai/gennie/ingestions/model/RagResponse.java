package com.enceladus.ai.gennie.rag.model;

import java.util.List;

public record RagResponse(
        String question,
        String answer,
        List<Source> sources
) {

    public record Source(
            String content
    ) {
    }
}

