package com.kidsworld.kidsping.domain.genre.service;

import com.kidsworld.kidsping.domain.genre.dto.response.TopGenreResponse;

import java.util.List;

public interface GenreScoreService {
    void addGenreScore(Long kidId, List<Long> genreIds);
    TopGenreResponse getTopGenre(Long kidId);
}
