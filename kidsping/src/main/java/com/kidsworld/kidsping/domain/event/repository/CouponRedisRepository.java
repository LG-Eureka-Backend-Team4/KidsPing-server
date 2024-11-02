package com.kidsworld.kidsping.domain.event.repository;

import com.kidsworld.kidsping.domain.event.dto.request.ApplyCouponRequest;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class CouponRedisRepository {

    private static final String EVENT_KEY = "EVENT:";
    private static final String EVENT_COUPON_COUNT = "COUPON:COUNT:EVENT:";
    private static final String USER_KEY = "USER:";
    private static final String MAX_COUPON_COUNT = "100";

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, String> hashOperations;

    @Autowired
    public CouponRedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();  // HashOperations 초기화
    }

    public Long applyCouponAtomically(ApplyCouponRequest request) {
        String luaScript =
                "local isMember = redis.call('SADD', KEYS[1], ARGV[1]) " +
                        "if isMember == 0 then " +
                        "    return 0 " +  // 이미 참여한 사용자임을 의미
                        "end " +

                        // 쿠폰 수량 증가 및 초과 확인
                        "local currentCount = redis.call('INCR', KEYS[2]) " +
                        "if currentCount > tonumber(ARGV[2]) then " +
                        "    redis.call('DECR', KEYS[2]) " +  // 수량 복구
                        "    redis.call('SREM', KEYS[1], ARGV[1]) " +  // 참여 취소
                        "    return -1 " +  // 쿠폰 초과
                        "end " +

                        // 쿠폰 신청 정보 저장
                        "redis.call('HMSET', KEYS[3], " +
                        "    'userId', ARGV[1], " +
                        "    'eventId', ARGV[3], " +
                        "    'name', ARGV[4], " +
                        "    'phone', ARGV[5]) " +
                        "return 1";  // 쿠폰 신청 성공

        List<String> keys = Arrays.asList(
                EVENT_KEY + request.getEventId(),
                EVENT_COUPON_COUNT + request.getEventId(),
                EVENT_KEY + request.getEventId() + USER_KEY + request.getUserId()
        );
        List<String> args = Arrays.asList(
                request.getUserId().toString(),
                MAX_COUPON_COUNT,  // 최대 발급 쿠폰 수
                request.getEventId().toString(),
                request.getName(),
                request.getPhone()
        );

        try {
            log.info("Executing Lua script with keys: {}", keys);
            log.info("Executing Lua script with args: {}", args);

            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
            Long result = redisTemplate.execute(redisScript, keys, args.toArray());

            log.info("DEBUG: EVENT_KEY SET members for key {}: {}", keys.get(0), redisTemplate.opsForSet().members(keys.get(0)));
            log.info("DEBUG: EVENT_COUPON_COUNT current value for key {}: {}", keys.get(1), redisTemplate.opsForValue().get(keys.get(1)));
            log.info("DEBUG: User Hash data for key {}: {}", keys.get(2), redisTemplate.opsForHash().entries(keys.get(2)));

            return result;

        } catch (Exception e) {
            log.info("쿠폰 발급 중 예외 발생 - 사용자ID {}, 이벤트 ID {}, 이름 {}, 전화번호 {}", request.getUserId(), request.getEventId(),
                    request.getName(), request.getPhone());
        }

        return 0L;
    }

    public Long verifyParticipation(Long eventId, Long userId) {
        String eventKey = EVENT_KEY + eventId;
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
        String couponKey = EVENT_KEY + request.getEventId() + USER_KEY + request.getUserId();
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
