package kidsworld.kidsping.consumer.domain.coupon.repository;

import kidsworld.kidsping.consumer.domain.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
