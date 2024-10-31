package com.kidsworld.kidsping.domain.event.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRedisRepository {

    private static final String EVENT_KEY_PREFIX = "EVENT:KEY:";
    private static final String EVENT_COUPON_COUNT = "COUPON:COUNT:EVENT:";

    private final RedisTemplate<String, String> redisTemplate;

    public Long add(Long eventId, Long userId) {
        String eventKey = EVENT_KEY_PREFIX + eventId;
        return redisTemplate
                .opsForSet()
                .add(eventKey, userId.toString());
    }

    public Long increment(Long eventId) {
        String eventKey = EVENT_COUPON_COUNT + eventId;
        return redisTemplate
                .opsForValue()
                .increment(eventKey);
    }

    // 등록된 key 모두 제거(테스트코드용)
    public void deleteByKey(String key) {
        redisTemplate.delete(key);
    }
}
