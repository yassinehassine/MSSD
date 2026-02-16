package com.mssd.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {

    private Long id;

    private String authorName;

    private String comment;

    private int rating;

    private LocalDateTime createdAt;

    private Long formationId;

    private String formationTitle;
}
