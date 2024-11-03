package com.kidsworld.kidsping.global.batch.scheduler;

import com.kidsworld.kidsping.domain.event.entity.Coupon;
import com.kidsworld.kidsping.domain.event.entity.Event;
import com.kidsworld.kidsping.domain.event.exception.EventNotFoundException;
import com.kidsworld.kidsping.domain.event.repository.CouponRedisRepository;
import com.kidsworld.kidsping.domain.event.repository.CouponRepository;
import com.kidsworld.kidsping.domain.event.repository.EventRepository;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.exception.UserNotFoundException;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CouponRepository couponRepository;
    private final CouponRedisRepository couponRedisRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Scheduled(cron = "0 0 4 * * *")
//  @Scheduled(cron = "*/10 * * * * *")  // 매 10초마다 실행 for Test
    @Transactional
    public void saveRedisDataToDatabase() {
        Set<String> keys = redisTemplate.keys("EVENT:*USER:*");

        if (keys != null) {
            for (String key : keys) {
                Map<Object, Object> data = redisTemplate.opsForHash().entries(key);

                try {
                    Long eventId = extractEventId(key);
                    Long userId = extractUserId(key);

                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UserNotFoundException());

                    Event event = eventRepository.findById(eventId)
                            .orElseThrow(() -> new EventNotFoundException());

                    Coupon coupon = Coupon.builder()
                            .user(user)
                            .event(event)
                            .name((String) data.get("name"))
                            .phone((String) data.get("phone"))
                            .build();

                    couponRepository.save(coupon);

                    // 데이터베이스에 저장이 완료되면 Redis 데이터 삭제
                    couponRedisRepository.deleteCouponKeys(eventId, userId);

                } catch (Exception e) {
                    log.error("[saveRedisDataToDatabase] Error processing key {}: {}", key, e.getMessage());
                    throw e;
                }
            }
        }
    }

    private Long extractEventId(String key) {
        String[] parts = key.split("EVENT:|USER:");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid key format: " + key);
        }
        return Long.parseLong(parts[1]);
    }

    private Long extractUserId(String key) {
        String[] parts = key.split("EVENT:|USER:");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid key format: " + key);
        }
        return Long.parseLong(parts[2]);
    }

}
