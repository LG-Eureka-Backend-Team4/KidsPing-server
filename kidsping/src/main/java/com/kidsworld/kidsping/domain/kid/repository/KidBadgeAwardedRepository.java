package com.kidsworld.kidsping.domain.kid.repository;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidBadge;
import com.kidsworld.kidsping.domain.kid.entity.KidBadgeAwarded;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KidBadgeAwardedRepository extends JpaRepository<KidBadgeAwarded, Long> {
    boolean existsByKidAndBadge(Kid kid, KidBadge badge);

    List<KidBadgeAwarded> findAllByKidId(Long kidId);

    @Modifying
    @Query(value = "delete from kid_badge_awarded kba where kba.is_deleted = true and kba.updated_at <= :currentDate - INTERVAL 1 MONTH", nativeQuery = true)
    void deleteExpiredGenreScore(@Param("currentDate") LocalDateTime currentDate);
}
