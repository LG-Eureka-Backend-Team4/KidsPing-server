package com.kidsworld.kidsping.domain.kid.repository;

import com.kidsworld.kidsping.domain.kid.entity.KidMbti;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KidMbtiRepository extends JpaRepository<KidMbti, Long> {

    @Query(value = "select km.kid_mbti_id from kid_mbti km where km.is_deleted = true and updated_at <= :currentDate - INTERVAL 1 MONTH", nativeQuery = true)
    List<Long> findExpiredKidMbtiIds(@Param("currentDate") LocalDateTime currentDate);

    @Modifying
    @Query("delete from KidMbti km where km.id in :expiredKidMbtiIds")
    void deleteExpiredKidMbti(@Param("expiredKidMbtiIds") List<Long> expiredKidMbtiIds);
}