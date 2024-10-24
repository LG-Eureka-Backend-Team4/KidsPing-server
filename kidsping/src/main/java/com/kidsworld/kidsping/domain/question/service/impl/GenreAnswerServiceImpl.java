package com.kidsworld.kidsping.domain.question.service.impl;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.repository.KidRepository;
import com.kidsworld.kidsping.domain.question.dto.response.GenreAnswerResponse;
import com.kidsworld.kidsping.domain.question.entity.GenreAnswer;
import com.kidsworld.kidsping.domain.question.repository.GenreAnswerRepository;
import com.kidsworld.kidsping.domain.question.service.GenreAnswerService;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import com.kidsworld.kidsping.global.exception.GlobalException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenreAnswerServiceImpl implements GenreAnswerService {

    private final GenreAnswerRepository genreAnswerRepository;
    private final KidRepository kidRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GenreAnswerResponse> getGenreAnswerHistory(Long kidId) {
        Kid kid = kidRepository.findById(kidId)
                .orElseThrow(() -> new GlobalException(ExceptionCode.NOT_FOUND_KID));

        return genreAnswerRepository.findByKidIdAndIsDeletedFalse(kidId)
                .stream()
                .map(GenreAnswerResponse::from)
                .collect(Collectors.toList());
    }

    /*
     * isDeleted 값이 true 이면서 날짜가 현재 기준으로 한달이 지난 id 값을 조회하는 메서드
     * */
    @Override
    public List<Long> findExpiredGenreAnswerIds() {
        return genreAnswerRepository.findExpiredGenreAnswerIds(LocalDateTime.now());
    }

    @Override
    public void deleteExpiredGenreAnswer(List<Long> expiredGenreAnswerIds) {
        genreAnswerRepository.deleteExpiredGenreAnswer(expiredGenreAnswerIds);
    }
}
