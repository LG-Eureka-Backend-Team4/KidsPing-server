package com.kidsworld.kidsping.domain.genre.repository;

import com.kidsworld.kidsping.domain.genre.entity.Genre;
import com.kidsworld.kidsping.domain.genre.entity.GenreScore;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GenreScoreRepository extends JpaRepository<GenreScore, Long> {
    Optional<GenreScore> findByKidAndGenre(Kid kid, Genre genre);

    @Query("SELECT gs FROM GenreScore gs " +
            "JOIN FETCH gs.genre g " +
            "WHERE gs.kid.id = :kidId " +
            "AND gs.isDeleted = false " +
            "ORDER BY gs.score DESC " +
            "LIMIT 1")
    Optional<GenreScore> findTopGenreByKidId(@Param("kidId") Long kidId);
}
