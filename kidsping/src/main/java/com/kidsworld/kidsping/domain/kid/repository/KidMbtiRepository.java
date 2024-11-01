package com.kidsworld.kidsping.domain.kid.repository;

import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KidMbtiRepository extends JpaRepository<KidMbti, Long> {

    @Modifying
    @Query(value = "delete from kid_mbti km where km.is_deleted = true and km.updated_at <= :currentDate - INTERVAL 1 MONTH", nativeQuery = true)
    void deleteExpiredKidMbti(@Param("currentDate") LocalDateTime currentDate);
}