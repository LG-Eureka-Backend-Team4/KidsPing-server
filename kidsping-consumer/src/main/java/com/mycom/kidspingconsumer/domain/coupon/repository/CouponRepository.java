package com.mycom.kidspingconsumer.domain.coupon.repository;

import com.mycom.kidspingconsumer.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
