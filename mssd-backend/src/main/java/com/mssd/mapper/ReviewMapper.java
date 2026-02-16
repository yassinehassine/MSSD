package com.mssd.mapper;

import com.mssd.dto.ReviewDto;
import com.mssd.model.Formation;
import com.mssd.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewDto toDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .authorName(review.getAuthorName())
                .comment(review.getComment())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .formationId(review.getFormation() != null ? review.getFormation().getId() : null)
                .formationTitle(review.getFormation() != null ? review.getFormation().getTitle() : null) // 👈 ajouter le titre
                .build();
    }


    public Review toEntity(ReviewDto dto, Formation formation) {
        return Review.builder()
                .id(dto.getId())
                .authorName(dto.getAuthorName())
                .comment(dto.getComment())
                .rating(dto.getRating())
                .formation(formation)
                .build();
    }
}
