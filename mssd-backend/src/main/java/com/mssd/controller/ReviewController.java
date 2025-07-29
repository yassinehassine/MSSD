package com.mssd.controller;

import com.mssd.dto.ReviewDto;
import com.mssd.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto dto) {
        ReviewDto created = reviewService.createReview(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/formation/{formationId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByFormation(@PathVariable Long formationId) {
        List<ReviewDto> reviews = reviewService.getReviewsByFormationId(formationId);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }
}
