package com.kidsworld.kidsping.domain.question.dto.response;

import com.kidsworld.kidsping.domain.question.entity.GenreAnswer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GenreAnswerResponseDto {
    private Long id;
    private Long kidId;
    private String content;
    private LocalDateTime createdAt;

    public static GenreAnswerResponseDto from(GenreAnswer genreAnswer) {
        return GenreAnswerResponseDto.builder()
                .id(genreAnswer.getId())
                .kidId(genreAnswer.getKid().getId())
                .content(genreAnswer.getContent())
                .createdAt(genreAnswer.getCreatedAt())
                .build();
    }
}
