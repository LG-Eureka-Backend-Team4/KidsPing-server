package com.kidsworld.kidsping.domain.kid.repository;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidMbtiHistory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KidMbtiHistoryRepository extends JpaRepository<KidMbtiHistory, Long> {

    //isDeleted 필드가 false인 history 5개만 조회
    @Query("SELECT h FROM KidMbtiHistory h " +
            "WHERE h.kid = :kid " +
            "AND h.isDeleted = false " +
            "ORDER BY h.createdAt DESC " +
            "LIMIT 5")
    List<KidMbtiHistory> findTop5ActiveHistories(@Param("kid") Kid kid);

    @Modifying
    @Query(value = "delete from kid_mbti_history kmh where kmh.is_deleted = true and kmh.updated_at <= :currentDate - INTERVAL 1 MONTH", nativeQuery = true)
    void deleteExpiredKidMbtiHistory(@Param("currentDate") LocalDateTime currentDate);
}
