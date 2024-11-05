package com.kidsworld.kidsping.domain.question.repository;

import com.kidsworld.kidsping.domain.question.entity.MbtiAnswer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MbtiAnswerRepository extends JpaRepository<MbtiAnswer, Long> {

    @Query("select ma.id from MbtiAnswer ma where ma.isDeleted = true")
    List<Long> findExpiredMbtiAnswerIds();

    @Modifying
    @Query(value = "delete from mbti_answer ma where ma.is_deleted = true and ma.updated_at <= :currentDate - INTERVAL 1 MONTH", nativeQuery = true)
    void deleteExpiredMbtiAnswer(@Param("currentDate") LocalDateTime currentDate);

    @Query("select ma from MbtiAnswer ma where ma.kid.id = :kidId and ma.isDeleted = false")
    Page<MbtiAnswer> findMbtiAnswersBy(@Param("kidId") Long kidId, Pageable pageable);

    @Query("select ma from MbtiAnswer ma where ma.id = :mbtiAnswerId and ma.isDeleted = false ")
    Optional<MbtiAnswer> findMbtiAnswerBy(@Param("mbtiAnswerId") Long mbtiAnswerId);
}