package com.mycom.kidspingconsumer.domain.coupon.repository;

import com.mycom.kidspingconsumer.domain.coupon.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}
