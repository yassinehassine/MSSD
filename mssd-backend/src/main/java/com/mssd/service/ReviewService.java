package com.mssd.service;

import com.mssd.dto.ReviewDto;
import com.mssd.model.Review;

import java.util.List;

public interface ReviewService {

    ReviewDto createReview(ReviewDto dto);

    List<Review> getReviewsByFormationId(Long formationId);
    void deleteReview(Long id);

    List<ReviewDto> getAllReviews();

}
