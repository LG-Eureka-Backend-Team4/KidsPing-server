package com.kidsworld.kidsping.domain.question.service.impl;

import com.kidsworld.kidsping.domain.question.dto.response.GenreAnswerResponseDto;
import com.kidsworld.kidsping.domain.question.entity.GenreAnswer;
import com.kidsworld.kidsping.domain.question.repository.GenreAnswerRepository;
import com.kidsworld.kidsping.domain.question.service.GenreAnswerService;
import com.kidsworld.kidsping.global.exception.GlobalException;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreAnswerServiceImpl implements GenreAnswerService {

    private final GenreAnswerRepository genreAnswerRepository;

    @Override
    @Transactional(readOnly = true)
    public GenreAnswerResponseDto getGenreAnswer(Long id) {
        GenreAnswer genreAnswer = genreAnswerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_GENRE_ANSWER));
        return GenreAnswerResponseDto.from(genreAnswer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreAnswerResponseDto> getGenreAnswersByKidId(Long kidId) {
        return genreAnswerRepository.findByKidIdAndIsDeletedFalse(kidId)
                .stream()
                .map(GenreAnswerResponseDto::from)
                .collect(Collectors.toList());
    }
}
