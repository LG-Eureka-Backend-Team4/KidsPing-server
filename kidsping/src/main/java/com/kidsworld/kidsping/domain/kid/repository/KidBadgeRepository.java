package com.kidsworld.kidsping.domain.kid.repository;

import com.kidsworld.kidsping.domain.kid.entity.KidBadge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KidBadgeRepository extends JpaRepository<KidBadge, Long> {
    KidBadge findByRequiredLevel(int requiredLevel);
}
