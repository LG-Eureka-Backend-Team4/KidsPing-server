package com.kidsworld.kidsping.domain.kid.repository;

import com.kidsworld.kidsping.domain.kid.entity.Kid;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KidRepository extends JpaRepository<Kid, Long> {
    long countByUserId(Long userId);

    @Query("select k from Kid k join fetch k.kidMbti where k.id = :kidId and k.isDeleted = false ")
    Optional<Kid> findKidWithMbtiByKidId(@Param("kidId") Long kidId);

    @Query("select k from Kid k where k.id = :kidId and k.isDeleted = false")
    Optional<Kid> findKidBy(@Param("kidId") Long kidId);

    @Modifying
    @Query(value = """
        UPDATE kid k
        LEFT JOIN kid_mbti km ON km.kid_id = k.kid_id
        LEFT JOIN kid_mbti_history kmh ON kmh.kid_id = k.kid_id
        LEFT JOIN mbti_answer ma ON ma.kid_id = k.kid_id
        SET k.is_deleted = true,
            km.is_deleted = CASE WHEN km.kid_id IS NOT NULL THEN true ELSE km.is_deleted END,
            kmh.is_deleted = CASE WHEN kmh.kid_id IS NOT NULL THEN true ELSE kmh.is_deleted END,
            ma.is_deleted = CASE WHEN ma.kid_id IS NOT NULL THEN true ELSE ma.is_deleted END
        WHERE k.kid_id = :kidId
        """, nativeQuery = true)
    void softDeleteKidAndRelatedData(@Param("kidId") Long kidId);


}