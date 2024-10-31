package com.kidsworld.kidsping.domain.event.repository;

import com.kidsworld.kidsping.domain.event.dto.request.ApplyCouponRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRedisRepository {

    private static final String EVENT_KEY_PREFIX = "EVENT:";
    private static final String EVENT_COUPON_COUNT = "COUPON:COUNT:EVENT:";
    private static final String USER_KEY_PREFIX = "USER:";

    private final RedisTemplate<String, String> redisTemplate;
    private final HashOperations<String, String, String> hashOperations;

    @Autowired
    public CouponRedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();  // HashOperations 초기화
    }

    public Long verifyParticipation(Long eventId, Long userId) {
        String eventKey = EVENT_KEY_PREFIX + eventId;
        return redisTemplate
                .opsForSet()
                .add(eventKey, userId.toString());
    }

    public Long incrementIssuedCouponCount(Long eventId) {
        String couponCount = EVENT_COUPON_COUNT + eventId;
        return redisTemplate
                .opsForValue()
                .increment(couponCount);
    }

    // 쿠폰 요청 데이터를 Redis에 저장
    public void saveApplyCoupon(ApplyCouponRequest request) {
        String couponKey = EVENT_KEY_PREFIX + request.getEventId() + USER_KEY_PREFIX + request.getUserId();
        hashOperations.put(couponKey, "userId", request.getUserId().toString());
        hashOperations.put(couponKey, "eventId", request.getEventId().toString());
        hashOperations.put(couponKey, "name", request.getName());
        hashOperations.put(couponKey, "phone", request.getPhone());
    }

    // 등록된 key 모두 제거(테스트코드용)
    public void deleteByKey(String key) {
        redisTemplate.delete(key);
    }
}
