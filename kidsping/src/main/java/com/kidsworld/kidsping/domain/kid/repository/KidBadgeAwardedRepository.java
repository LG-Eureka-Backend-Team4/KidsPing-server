package com.kidsworld.kidsping.domain.kid.repository;

import com.kidsworld.kidsping.domain.kid.entity.Kid;
import com.kidsworld.kidsping.domain.kid.entity.KidBadge;
import com.kidsworld.kidsping.domain.kid.entity.KidBadgeAwarded;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KidBadgeAwardedRepository extends JpaRepository<KidBadgeAwarded, Long> {
    boolean existsByKidAndBadge(Kid kid, KidBadge badge);

    List<KidBadgeAwarded> findAllByKidId(Long kidId);
}
