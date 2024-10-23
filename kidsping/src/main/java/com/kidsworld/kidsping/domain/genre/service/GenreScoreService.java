package com.kidsworld.kidsping.domain.genre.service;

import java.util.List;

public interface GenreScoreService {
    void addGenreScore(Long kidId, List<Long> genreIds);
}
