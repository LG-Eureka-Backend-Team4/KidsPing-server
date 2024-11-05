package com.kidsworld.kidsping.domain.question.service;

import com.kidsworld.kidsping.domain.question.dto.response.GenreAnswerResponse;
import java.util.List;

public interface GenreAnswerService {

    void deleteExpiredGenreAnswer();

    List<GenreAnswerResponse> getGenreAnswerHistory(Long kidId);
}