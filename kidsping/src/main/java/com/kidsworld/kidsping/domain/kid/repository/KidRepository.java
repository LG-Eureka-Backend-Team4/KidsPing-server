package com.kidsworld.kidsping.domain.kid.repository;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KidRepository extends JpaRepository<Kid, Long> {
    long countByUserId(Long userId);

    @Query("select k from Kid k join fetch k.kidMbti where k.id = :kidId and k.isDeleted = false ")
    Optional<Kid> findKidWithMbtiByKidId(@Param("kidId") Long kidId);

    @Query("select k from Kid k where k.id = :kidId and k.isDeleted = false")
    Optional<Kid> findKidBy(@Param("kidId") Long kidId);
}