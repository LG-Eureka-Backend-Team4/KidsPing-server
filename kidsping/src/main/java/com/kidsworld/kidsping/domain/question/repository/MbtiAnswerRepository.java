package com.kidsworld.kidsping.domain.question.repository;

import com.kidsworld.kidsping.domain.question.entity.MbtiAnswer;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MbtiAnswerRepository extends JpaRepository<MbtiAnswer, Long> {

    @Query(value = "select ma.mbti_answer_id from mbti_answer ma where ma.is_deleted = true and updated_at <= :currentDate - INTERVAL 1 MONTH", nativeQuery = true)
    List<Long> findExpiredMbtiAnswerIds(@Param("currentDate") LocalDateTime currentDate);

    @Modifying
    @Query("delete from MbtiAnswer ma where ma.id IN :expiredMbtiAnswerIds")
    void deleteExpiredMbtiAnswer(@Param("expiredMbtiAnswerIds") List<Long> expiredMbtiAnswerIds);
}