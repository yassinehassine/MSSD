package com.mssd.service;

import com.mssd.dto.ReviewDto;

import java.util.List;

public interface ReviewService {

    ReviewDto createReview(ReviewDto dto);

    List<ReviewDto> getReviewsByFormationId(Long formationId);

    void deleteReview(Long id);

    List<ReviewDto> getAllReviews();
}
