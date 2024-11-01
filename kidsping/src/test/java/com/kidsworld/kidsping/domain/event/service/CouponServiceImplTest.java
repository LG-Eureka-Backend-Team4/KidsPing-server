package com.kidsworld.kidsping.domain.event.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.kidsworld.kidsping.domain.event.dto.request.ApplyCouponRequest;
import com.kidsworld.kidsping.domain.event.entity.Event;
import com.kidsworld.kidsping.domain.event.repository.CouponRedisRepository;
import com.kidsworld.kidsping.domain.event.repository.CouponRepository;
import com.kidsworld.kidsping.domain.event.repository.EventRepository;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.entity.enums.Role;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRedisRepository couponRedisRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventRepository eventRepository;
    
    @BeforeEach
    void tearDown() {
        // Redis 키 제거
        couponRepository.deleteAllInBatch();
        eventRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        couponRedisRepository.deleteByKey("EVENT_KEY_1");
        couponRedisRepository.deleteByKey("EVENT_COUPON_COUNT_1");
    }

    @Test
    @DisplayName("사용자가 한 번 응모 시 단일 쿠폰이 발급되는지 검증")
    public void shouldIssueSingleCouponWhenUserAppliesOnce() {
        createUser(1);
        createEvent();

        ApplyCouponRequest applyCouponRequest = applyCoupon(1L, 1L, "이름", "번호");

        couponService.applyCoupon(applyCouponRequest);
        long count = couponRepository.count();

        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("동시에 1000명의 사용자가 이벤트 응모 시 중복 없이 100개의 쿠폰이 발급되는지 테스트")
    public void concurrentCouponApplicationWith1000Users_shouldIssueOnly100Coupons() throws InterruptedException {
        createEvent();
        // 1000명의 유저 저장
        for (int i = 0; i < 1000; i++) {
            createUser(i);
        }

        Thread.sleep(2000);

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(16);
        CountDownLatch latch = new CountDownLatch(threadCount);

        int startUserId = 1;
        for (int i = 0; i < threadCount; i++) {
            Long userId = (long) (startUserId + i);
            executorService.submit(() -> {
                try {
                    ApplyCouponRequest applyCouponRequest = applyCoupon(1L, userId, "이름" + userId, "번호" + userId);
                    couponService.applyCoupon(applyCouponRequest);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Thread.sleep(2000);

        long count = couponRepository.count();
        assertThat(count).isEqualTo(100);

        couponRedisRepository.deleteByKey("EVENT_KEY_1");
        couponRedisRepository.deleteByKey("EVENT_COUPON_COUNT_1");
    }

    @Test
    @DisplayName("동일한 사용자가 중복 요청 시 단일 쿠폰만 발급되는지 검증")
    public void shouldIssueOnlyOneCouponPerUserWhenMultipleRequestsAreMade() throws InterruptedException {
        createUser(1);
        createEvent();

        ApplyCouponRequest applyCouponRequest = applyCoupon(1L, 1L, "이름2", "번호2");
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(16);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    couponService.applyCoupon(applyCouponRequest);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Thread.sleep(2000);

        long count = couponRepository.count();
        assertThat(count).isEqualTo(1);
    }

    private void createUser(int id) {
        User user = User.builder()
                .email("user" + id + "@example.com")
                .password("password" + id)
                .userName("User" + id)
                .phone("010-1234-" + String.format("%04d", id))
                .role(Role.USER) // Role에 따라 적절한 값 설정
                .isDeleted(false)
                .build();
        userRepository.save(user);
    }

    private void createEvent() {
        Event event = Event.builder()
                .eventContent("이벤트 내용")
                .eventName("이벤트 이름")
                .build();
        eventRepository.save(event);
    }

    private static ApplyCouponRequest applyCoupon(Long eventId, Long userId, String name, String phone) {
        return ApplyCouponRequest
                .builder()
                .eventId(eventId)
                .userId(userId)
                .name(name)
                .phone(phone)
                .build();
    }
}