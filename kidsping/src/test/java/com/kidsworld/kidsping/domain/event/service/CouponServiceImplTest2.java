package com.kidsworld.kidsping.domain.event.service;

import com.kidsworld.kidsping.domain.event.dto.request.CheckWinnerRequest;
import com.kidsworld.kidsping.domain.event.dto.response.CheckWinnerResponse;
import com.kidsworld.kidsping.domain.event.entity.Coupon;
import com.kidsworld.kidsping.domain.event.entity.Event;
import com.kidsworld.kidsping.domain.event.repository.CouponRedisRepository;
import com.kidsworld.kidsping.domain.event.repository.CouponRepository;
import com.kidsworld.kidsping.domain.event.repository.EventRepository;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest2 {

    @InjectMocks
    private CouponServiceImpl couponService;

    @Mock
    private CouponRepository couponRepository;

    private User mockUser;
    private Event mockEvent;

    @BeforeEach
    void setUp() {
        mockUser = User.builder().id(1L).build();
        mockEvent = Event.builder().id(1L).build();
    }

    @Test
    @DisplayName("당첨 확인: 쿠폰을 소지하고 있는 경우")
    void checkWinner_쿠폰소지자() {
        // Given
        Coupon coupon = Coupon.builder().user(mockUser).event(mockEvent).build();

        when(couponRepository.findByUserIdAndEventId(mockUser.getId(), mockEvent.getId()))
                .thenReturn(Optional.of(coupon));

        CheckWinnerRequest request = CheckWinnerRequest.builder()
                .userId(mockUser.getId())
                .eventId(mockEvent.getId())
                .build();

        // When
        CheckWinnerResponse response = couponService.checkWinner(request);

        // Then
        assertThat(response.isWinningYn()).isTrue();
    }

    @Test
    @DisplayName("당첨 확인: 쿠폰을 소지하고 있지 않은 경우")
    void checkWinner_쿠폰미소지자() {
        // Given
        when(couponRepository.findByUserIdAndEventId(mockUser.getId(), mockEvent.getId()))
                .thenReturn(Optional.empty());

        CheckWinnerRequest request = CheckWinnerRequest.builder()
                .userId(mockUser.getId())
                .eventId(mockEvent.getId())
                .build();

        // When
        CheckWinnerResponse response = couponService.checkWinner(request);

        // Then
        assertThat(response.isWinningYn()).isFalse();
    }
}