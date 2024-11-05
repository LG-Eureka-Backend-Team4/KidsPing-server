package com.kidsworld.kidsping.domain.mbti.repository;

import com.kidsworld.kidsping.domain.mbti.entity.MbtiInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface MbtiInfoRepository extends JpaRepository<MbtiInfo, Long> {

    // isDeleted 필드가 false인 MbtiInfo 찾기
    @Query("SELECT m FROM MbtiInfo m WHERE m.isDeleted = false AND m.id = :mbtiInfoId")
    Optional<MbtiInfo> findActiveMbtiInfo(Long mbtiInfoId);
}
