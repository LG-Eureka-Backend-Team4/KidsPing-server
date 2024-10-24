package com.kidsworld.kidsping.domain.question.repository;

import com.kidsworld.kidsping.domain.question.entity.GenreAnswer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreAnswerRepository extends JpaRepository<GenreAnswer, Long> {
  
    List<GenreAnswer> findByKidIdAndIsDeletedFalse(Long kidId);

    Optional<GenreAnswer> findByIdAndIsDeletedFalse(Long id);

    @Query(value = "select ga.genre_answer_id from genre_answer ga where ga.is_deleted = true and updated_at <= :currentDate - INTERVAL 1 MONTH", nativeQuery = true)
    List<Long> findExpiredGenreAnswerIds(@Param("currentDate") LocalDateTime currentDate);

    @Modifying
    @Query("delete from GenreAnswer ga where ga.id in :expiredGenreAnswerIds")
    void deleteExpiredGenreAnswer(@Param("expiredGenreAnswerIds") List<Long> expiredGenreAnswerIds);
  
    @Query("SELECT ga FROM GenreAnswer ga " +
            "WHERE ga.kid.id = :kidId " +
            "AND ga.isDeleted = false " +
            "ORDER BY ga.createdAt DESC")
    List<GenreAnswer> findByKidIdAndIsDeletedFalse(@Param("kidId") Long kidId);
}
