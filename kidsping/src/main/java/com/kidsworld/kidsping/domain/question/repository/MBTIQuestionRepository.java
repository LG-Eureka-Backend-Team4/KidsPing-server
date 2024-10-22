package com.kidsworld.kidsping.domain.question.repository;

import com.kidsworld.kidsping.domain.question.entity.MBTIQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MBTIQuestionRepository extends JpaRepository<MBTIQuestion, Long> {
}
