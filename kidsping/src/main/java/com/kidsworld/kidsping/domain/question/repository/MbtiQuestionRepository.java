package com.kidsworld.kidsping.domain.question.repository;

import com.kidsworld.kidsping.domain.question.entity.MbtiQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MbtiQuestionRepository extends JpaRepository<MbtiQuestion, Long> {
}
