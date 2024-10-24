package com.kidsworld.kidsping.domain.question.service;

import com.kidsworld.kidsping.domain.question.dto.response.GenreAnswerResponse;
import java.util.List;

public interface GenreAnswerService {

    GenreAnswerResponse getGenreAnswer(Long id);

    List<GenreAnswerResponse> getGenreAnswersByKidId(Long kidId);

    /*
     * isDeleted 값이 true 이면서 날짜가 현재 기준으로 한달이 지난 id 값을 조회하는 메서드
     * */
    List<Long> findExpiredGenreAnswerIds();

    void deleteExpiredGenreAnswer(List<Long> expiredGenreAnswerIds);

    List<GenreAnswerResponse> getGenreAnswerHistory(Long kidId);
}