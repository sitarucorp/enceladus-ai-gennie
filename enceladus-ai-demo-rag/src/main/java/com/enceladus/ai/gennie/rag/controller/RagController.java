package com.enceladus.ai.gennie.rag.controller;

import com.enceladus.ai.gennie.rag.model.RagResponse;
import com.enceladus.ai.gennie.rag.service.RagService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping
    public RagResponse ask(@RequestParam String question) {
        return ragService.ask(question);
    }
}