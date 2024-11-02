package com.kidsworld.kidsping.global.batch.scheduler;

import com.kidsworld.kidsping.domain.event.dto.request.ApplyCouponRequest;
import com.kidsworld.kidsping.domain.event.entity.Coupon;
import com.kidsworld.kidsping.domain.event.entity.Event;
import com.kidsworld.kidsping.domain.event.repository.CouponRedisRepository;
import com.kidsworld.kidsping.domain.event.repository.CouponRepository;
import com.kidsworld.kidsping.domain.event.repository.EventRepository;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
//@Transactional
public class CouponSchedulerTest2 {

    @Autowired
    private CouponScheduler couponScheduler;

    @Autowired
    private CouponRedisRepository couponRedisRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    private User testUser;
    private Event testEvent;

    @BeforeEach
    public void setup() {
        testUser = User.builder().userName("Test User").build();
        testEvent = Event.builder().eventName("Test Event").build();

        testUser = userRepository.save(testUser);
        testEvent = eventRepository.save(testEvent);

        ApplyCouponRequest request = ApplyCouponRequest.builder()
                .userId(testUser.getId())
                .eventId(testEvent.getId())
                .name("응모자 이름")
                .phone("01012345678")
                .build();
        couponRedisRepository.applyCouponAtomically(request);
    }

    @Test
    public void testSaveRedisDataToDatabase() {
        couponScheduler.saveRedisDataToDatabase(); // 수동 호출

        Optional<Coupon> savedCoupon = couponRepository.findAll().stream().findFirst();
        assertThat(savedCoupon).isPresent();

        Coupon coupon = savedCoupon.get();
        assertThat(coupon.getName()).isEqualTo("응모자 이름");
        assertThat(coupon.getPhone()).isEqualTo("01012345678");
    }
}