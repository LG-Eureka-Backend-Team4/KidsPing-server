package com.kidsworld.kidsping.domain.question.repository;

import com.kidsworld.kidsping.domain.question.entity.MbtiResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MbtiResponseRepository extends JpaRepository<MbtiResponse, Long> {
}
