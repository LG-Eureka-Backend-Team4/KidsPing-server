package com.kidsworld.kidsping.domain.question.repository;

import com.kidsworld.kidsping.domain.question.entity.GenreAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreAnswerRepository extends JpaRepository<GenreAnswer, Long> {
    @Query("SELECT ga FROM GenreAnswer ga " +
            "WHERE ga.kid.id = :kidId " +
            "AND ga.isDeleted = false " +
            "ORDER BY ga.createdAt DESC")
    List<GenreAnswer> findByKidIdAndIsDeletedFalse(@Param("kidId") Long kidId);
}
