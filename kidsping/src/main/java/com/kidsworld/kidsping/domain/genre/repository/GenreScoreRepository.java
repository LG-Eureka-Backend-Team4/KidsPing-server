package com.kidsworld.kidsping.domain.genre.repository;

import com.kidsworld.kidsping.domain.genre.entity.Genre;
import com.kidsworld.kidsping.domain.genre.entity.GenreScore;
import com.kidsworld.kidsping.domain.kid.entity.Kid;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GenreScoreRepository extends JpaRepository<GenreScore, Long> {
    Optional<GenreScore> findByKidAndGenre(Kid kid, Genre genre);

    @Query("SELECT gs FROM GenreScore gs " +
            "JOIN FETCH gs.genre g " +
            "WHERE gs.kid.id = :kidId " +
            "AND gs.isDeleted = false " +
            "ORDER BY gs.score DESC " +
            "LIMIT 1")
    Optional<GenreScore> findTopGenreByKidId(@Param("kidId") Long kidId);

    @Query("SELECT gs.genre FROM GenreScore gs " +
            "GROUP BY gs.genre " +
            "ORDER BY SUM(gs.score) DESC " +
            "LIMIT 1")
    Optional<Genre> findTopGenre();

    @Modifying
    @Query("delete from GenreScore gs where gs.kid.id = :kidId")
    void deleteGenreScoresByKidId(@Param("kidId") Long kidId);

    @Modifying
    @Query(value = "delete from genre_score gs where gs.is_deleted = true and gs.updated_at <= :currentDate - INTERVAL 1 MONTH", nativeQuery = true)
    void deleteExpiredGenreScore(@Param("currentDate") LocalDateTime currentDate);
}
