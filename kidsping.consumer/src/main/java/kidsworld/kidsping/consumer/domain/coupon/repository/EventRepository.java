package kidsworld.kidsping.consumer.domain.coupon.repository;

import kidsworld.kidsping.consumer.domain.coupon.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long>  {
}
