package com.kidsworld.kidsping.domain.kid.repository;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidMbtiHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KidMbtiHistoryRepository extends JpaRepository<KidMbtiHistory, Long> {

    //isDeleted 필드가 false인 history 찾기
    @Query("SELECT h FROM KidMbtiHistory h " +
            "WHERE h.kid = :kid AND h.isDeleted = false " +
            "ORDER BY h.createdAt DESC")
    List<KidMbtiHistory> findActiveHistories(@Param("kid") Kid kid);


}
