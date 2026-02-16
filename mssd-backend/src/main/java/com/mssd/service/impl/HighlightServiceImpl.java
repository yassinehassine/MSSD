package com.mssd.service.impl;

import com.mssd.model.Highlight;
import com.mssd.repository.HighlightRepository;
import com.mssd.service.HighlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HighlightServiceImpl implements HighlightService {
    private final HighlightRepository highlightRepository;

    @Override
    public List<Highlight> getVisibleHighlights() {
        return highlightRepository.findByVisibleTrue();
    }
} 