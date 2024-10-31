package kidsworld.kidsping.consumer.domain.coupon.repository;

import kidsworld.kidsping.consumer.domain.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Query("SELECT COUNT(c) FROM Coupon c WHERE c.event.id = :eventId")
    long countByEventId(@Param("eventId") Long eventId);
}