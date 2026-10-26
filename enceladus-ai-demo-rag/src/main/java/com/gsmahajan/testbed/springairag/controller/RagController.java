package com.gsmahajan.testbed.springairag.controller;

import com.gsmahajan.testbed.springairag.model.RagResponse;
import com.gsmahajan.testbed.springairag.service.RagService;
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