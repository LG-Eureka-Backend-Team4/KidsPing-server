package com.kidsworld.kidsping.domain.kid.repository;

import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KidMbtiRepository extends JpaRepository<KidMbti, Long> {

    @Modifying
    @Query(value = "delete from kid_mbti km where km.is_deleted = true and km.updated_at <= :currentDate - INTERVAL 1 MONTH", nativeQuery = true)
    void deleteExpiredKidMbti(@Param("currentDate") LocalDateTime currentDate);

    @Query("select km from KidMbti km where km.kid.id = :kidId and km.isDeleted = false ")
    Optional<KidMbti> findKidMbtiBy(@Param("kidId") Long kidId);
}