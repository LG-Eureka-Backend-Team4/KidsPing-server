package com.mycom.kidspingconsumer.domain.coupon.repository;

import com.mycom.kidspingconsumer.domain.coupon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}
