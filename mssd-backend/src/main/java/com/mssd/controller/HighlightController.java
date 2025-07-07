package com.mssd.controller;

import com.mssd.model.Highlight;
import com.mssd.service.HighlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/highlights")
@RequiredArgsConstructor
public class HighlightController {
    private final HighlightService highlightService;

    @GetMapping
    public List<Highlight> getVisibleHighlights() {
        return highlightService.getVisibleHighlights();
    }
} 