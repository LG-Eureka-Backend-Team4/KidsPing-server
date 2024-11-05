package com.kidsworld.kidsping.domain.question.dto.response;

import com.kidsworld.kidsping.domain.question.entity.GenreAnswer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GenreAnswerResponse {
    private Long kidId;
    private LocalDateTime createdAt;

    public static GenreAnswerResponse from(GenreAnswer genreAnswer) {
        return GenreAnswerResponse.builder()
                .kidId(genreAnswer.getKid().getId())
                .createdAt(genreAnswer.getCreatedAt())
                .build();
    }
}
