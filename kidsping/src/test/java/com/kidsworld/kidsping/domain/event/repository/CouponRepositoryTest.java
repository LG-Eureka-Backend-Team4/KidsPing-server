package com.kidsworld.kidsping.domain.event.repository;

import com.kidsworld.kidsping.domain.event.entity.Coupon;
import com.kidsworld.kidsping.domain.event.entity.Event;
import com.kidsworld.kidsping.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponRepositoryTest {

    @Mock
    private CouponRepository couponRepository;

    private User mockUser;
    private Event mockEvent;
    private Coupon mockCoupon;

    @BeforeEach
    void setUp() {
        mockUser = User.builder().id(1L).build();
        mockEvent = Event.builder().id(1L).build();

        mockCoupon = Coupon.builder()
                .id(1L)
                .user(mockUser)
                .event(mockEvent)
                .name("Test User")
                .phone("010-1234-5678")
                .build();
    }

    @Test
    void findByUserIdAndEventId_쿠폰조회성공() {
        // Given
        Long userId = mockUser.getId();
        Long eventId = mockEvent.getId();

        when(couponRepository.findByUserIdAndEventId(userId, eventId))
                .thenReturn(Optional.of(mockCoupon));

        // When
        Optional<Coupon> result = couponRepository.findByUserIdAndEventId(userId, eventId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(mockCoupon.getId(), result.get().getId());
        assertEquals(mockCoupon.getName(), result.get().getName());
    }

    @Test
    void findByUserIdAndEventId_쿠폰조회실패() {
        // Given
        Long userId = mockUser.getId();
        Long eventId = 9999L; // 존재하지 않는 이벤트 ID

        when(couponRepository.findByUserIdAndEventId(userId, eventId))
                .thenReturn(Optional.empty());

        // When
        Optional<Coupon> result = couponRepository.findByUserIdAndEventId(userId, eventId);

        // Then
        assertFalse(result.isPresent());
    }
}