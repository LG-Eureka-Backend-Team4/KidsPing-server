package kidsworld.kidsping.consumer.domain.coupon.repository;

import kidsworld.kidsping.consumer.domain.coupon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}
