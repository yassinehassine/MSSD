package com.mssd.controller;

import com.mssd.dto.NewsletterDto;
import com.mssd.service.NewsletterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/newsletter")
@RequiredArgsConstructor
public class NewsletterController {
    private final NewsletterService newsletterService;

    @PostMapping
    public ResponseEntity<Void> subscribe(@Valid @RequestBody NewsletterDto newsletterDto) {
        newsletterService.subscribe(newsletterDto);
        return ResponseEntity.ok().build();
    }
} 