package com.kidsworld.kidsping.domain.genre.service;

import com.kidsworld.kidsping.domain.genre.entity.Genre;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.like.entity.enums.LikeStatus;

import java.util.List;

public interface GenreScoreService {
    void addGenreScore(Long kidId, List<Long> genreIds);
    void updateScore(Kid kid, Genre genre, LikeStatus previousStatus, LikeStatus newStatus);
}
