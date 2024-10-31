package com.mycom.kidspingconsumer.domain.coupon.repository;

import com.mycom.kidspingconsumer.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Query("SELECT COUNT(c) FROM Coupon c WHERE c.event.id = :eventId")
    long countByEventId(@Param("eventId") Long eventId);
}
