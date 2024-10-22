package com.kidsworld.kidsping.domain.kid.repository;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface KidRepository extends JpaRepository<Kid, Long> {
    long countByUserId(Long userId);

    // 특정 사용자의 자녀들 중 비어있는 가장 작은 ID 찾기
    @Query("SELECT k.id FROM Kid k WHERE k.user.id = :userId ORDER BY k.id")
    List<Long> findKidIdsByUserId(@Param("userId") Long userId);

}