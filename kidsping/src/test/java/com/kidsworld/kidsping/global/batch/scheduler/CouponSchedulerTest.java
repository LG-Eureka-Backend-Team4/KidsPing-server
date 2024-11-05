package com.kidsworld.kidsping.global.batch.scheduler;

import com.kidsworld.kidsping.domain.event.entity.Coupon;
import com.kidsworld.kidsping.domain.event.entity.Event;
import com.kidsworld.kidsping.domain.event.exception.EventNotFoundException;
import com.kidsworld.kidsping.domain.event.repository.CouponRepository;
import com.kidsworld.kidsping.domain.event.repository.EventRepository;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.exception.UserNotFoundException;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponSchedulerTest {

    @InjectMocks
    private CouponScheduler couponScheduler;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    private User mockUser;
    private Event mockEvent;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(redisTemplate.keys("event:*user:*")).thenReturn(Set.of("event:9999user:9999"));

        mockUser = User.builder().id(9999L).build();
        mockEvent = Event.builder().id(9999L).build();
    }

    @Test
    void saveRedisDataToDatabase_쿠폰저장성공() {
        // Given
        String key = "event:9999user:9999";
        Set<String> keys = Set.of(key);

        when(redisTemplate.keys("event:*user:*")).thenReturn(keys);

        Map<Object, Object> data = new HashMap<>();
        data.put("name", "User9999");
        data.put("phone", "01012341234");
        when(hashOperations.entries(key)).thenReturn(data);

        when(userRepository.findById(9999L)).thenReturn(Optional.of(mockUser));
        when(eventRepository.findById(9999L)).thenReturn(Optional.of(mockEvent));

        // When
        couponScheduler.saveRedisDataToDatabase();

        // Then
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @Test
    void saveRedisDataToDatabase_UserNotFoundException발생() {
        // Given
        String key = "event:9999user:999999"; // 유효하지 않은 userId 사용
        Set<String> keys = Set.of(key);

        when(redisTemplate.keys("event:*user:*")).thenReturn(keys);

        Map<Object, Object> data = new HashMap<>();
        data.put("name", "User99999");
        data.put("phone", "01012341234");
        when(hashOperations.entries(key)).thenReturn(data);

        when(userRepository.findById(999999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> couponScheduler.saveRedisDataToDatabase());
    }

    @Test
    void saveRedisDataToDatabase_EventNotFoundException발생() {
        // Given
        String key = "event:999999user:9999"; // 유효하지 않은 eventId 사용
        Set<String> keys = Set.of(key);

        when(redisTemplate.keys("event:*user:*")).thenReturn(keys);

        Map<Object, Object> data = new HashMap<>();
        data.put("name", "User9999");
        data.put("phone", "01012341234");
        when(hashOperations.entries(key)).thenReturn(data);

        when(userRepository.findById(9999L)).thenReturn(Optional.of(mockUser));
        when(eventRepository.findById(999999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EventNotFoundException.class, () -> couponScheduler.saveRedisDataToDatabase());
    }
}