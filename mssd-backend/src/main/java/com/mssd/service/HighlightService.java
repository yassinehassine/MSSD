package com.mssd.service;

import com.mssd.model.Highlight;
import java.util.List;

public interface HighlightService {
    List<Highlight> getVisibleHighlights();
} 