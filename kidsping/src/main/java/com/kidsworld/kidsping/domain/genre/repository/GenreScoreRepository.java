package com.kidsworld.kidsping.domain.genre.repository;

import com.kidsworld.kidsping.domain.genre.entity.Genre;
import com.kidsworld.kidsping.domain.genre.entity.GenreScore;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreScoreRepository extends JpaRepository<GenreScore, Long> {
    Optional<GenreScore> findByKidAndGenre(Kid kid, Genre genre);
}
