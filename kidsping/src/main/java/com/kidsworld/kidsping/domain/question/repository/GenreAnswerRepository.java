package com.kidsworld.kidsping.domain.question.repository;

import com.kidsworld.kidsping.domain.question.entity.GenreAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreAnswerRepository extends JpaRepository<GenreAnswer, Long> {
    List<GenreAnswer> findByKidIdAndIsDeletedFalse(Long kidId);
    Optional<GenreAnswer> findByIdAndIsDeletedFalse(Long id);
}
