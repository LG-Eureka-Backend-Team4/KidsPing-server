package com.kidsworld.kidsping.domain.question.dto.response;

import com.kidsworld.kidsping.domain.question.entity.GenreAnswer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GenreAnswerResponse {
    private Long id;
    private Long kidId;
    private String content;
    private LocalDateTime createdAt;

    public static GenreAnswerResponse from(GenreAnswer genreAnswer) {
        return GenreAnswerResponse.builder()
                .id(genreAnswer.getId())
                .kidId(genreAnswer.getKid().getId())
                .content(genreAnswer.getContent())
                .createdAt(genreAnswer.getCreatedAt())
                .build();
    }
}
