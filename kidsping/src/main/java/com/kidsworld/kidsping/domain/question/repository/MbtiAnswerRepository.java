package com.kidsworld.kidsping.domain.question.repository;

import com.kidsworld.kidsping.domain.question.entity.MbtiAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MbtiAnswerRepository extends JpaRepository<MbtiAnswer, Long> {
}
