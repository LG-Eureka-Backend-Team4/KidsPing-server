package com.kidsworld.kidsping.domain.question.service;

import com.kidsworld.kidsping.domain.question.dto.response.GenreAnswerResponseDto;

import java.util.List;

public interface GenreAnswerService {
    GenreAnswerResponseDto getGenreAnswer(Long id);
    List<GenreAnswerResponseDto> getGenreAnswersByKidId(Long kidId);
}
