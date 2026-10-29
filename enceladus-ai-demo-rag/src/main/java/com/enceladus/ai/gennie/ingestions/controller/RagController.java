package com.enceladus.ai.gennie.ingestions.controller;

import com.enceladus.ai.gennie.ingestions.model.RagResponse;
import com.enceladus.ai.gennie.ingestions.service.RagService;
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