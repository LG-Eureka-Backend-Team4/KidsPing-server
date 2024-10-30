package com.kidsworld.kidsping.domain.event.repository;

import com.kidsworld.kidsping.domain.event.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventWinnerRepository extends JpaRepository<Coupon,Long> {
    Optional<Coupon> findByUserIdAndEventId(Long userId, Long eventId);
}
