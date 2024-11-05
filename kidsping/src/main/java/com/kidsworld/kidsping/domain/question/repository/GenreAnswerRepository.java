package com.kidsworld.kidsping.domain.question.repository;

import com.kidsworld.kidsping.domain.question.entity.GenreAnswer;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreAnswerRepository extends JpaRepository<GenreAnswer, Long> {

    @Modifying
    @Query(value = "delete from genre_answer ga where ga.is_deleted = true and ga.updated_at <= :currentDate - INTERVAL 1 MONTH", nativeQuery = true)
    void deleteExpiredGenreAnswer(@Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT ga FROM GenreAnswer ga " +
            "WHERE ga.kid.id = :kidId " +
            "AND ga.isDeleted = false " +
            "ORDER BY ga.createdAt DESC")
    List<GenreAnswer> findByKidIdAndIsDeletedFalse(@Param("kidId") Long kidId);
}
