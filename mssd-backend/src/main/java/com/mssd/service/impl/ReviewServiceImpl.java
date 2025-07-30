package com.mssd.service.impl;

import com.mssd.dto.ReviewDto;
import com.mssd.exception.ResourceNotFoundException;
import com.mssd.mapper.ReviewMapper;
import com.mssd.model.Formation;
import com.mssd.model.Review;
import com.mssd.repository.FormationRepository;
import com.mssd.repository.ReviewRepository;
import com.mssd.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final FormationRepository formationRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewDto createReview(ReviewDto dto) {
        Formation formation = formationRepository.findById(dto.getFormationId())
                .orElseThrow(() -> new ResourceNotFoundException("Formation not found with ID: " + dto.getFormationId()));

        Review review = reviewMapper.toEntity(dto, formation);
        review.setCreatedAt(LocalDateTime.now());
        Review saved = reviewRepository.save(review);
        return reviewMapper.toDto(saved);
    }

    @Override
    public List<Review> getReviewsByFormationId(Long formationId) {
        return reviewRepository.findByFormation_Id(formationId);
    }


    @Override
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review not found with ID: " + id);
        }
        reviewRepository.deleteById(id);
    }

    @Override
    public List<ReviewDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAllWithFormation(); // ⬅️ Utiliser cette méthode
        return reviews.stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

}
