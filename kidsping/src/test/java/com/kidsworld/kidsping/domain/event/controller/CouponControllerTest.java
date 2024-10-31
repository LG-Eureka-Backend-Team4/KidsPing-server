package com.kidsworld.kidsping.domain.event.controller;

import com.kidsworld.kidsping.domain.event.dto.request.ApplyCouponRequest;
import com.kidsworld.kidsping.domain.event.dto.request.CheckWinnerRequest;
import com.kidsworld.kidsping.domain.event.dto.response.ApplyCouponResponse;
import com.kidsworld.kidsping.domain.event.dto.response.CheckWinnerResponse;
import com.kidsworld.kidsping.domain.event.service.CouponService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponControllerTest {

    @InjectMocks
    private CouponController couponController;

    @Mock
    private CouponService couponService;

    @Test
    @DisplayName("쿠폰 신청 테스트")
    void applyCoupon_이벤트참여() {
        // Given
        ApplyCouponRequest request = ApplyCouponRequest.builder()
                .eventId(1L)
                .userId(1L)
                .name("Test User")
                .phone("01012345678")
                .build();

        // When
        ResponseEntity<ApiResponse<ApplyCouponResponse>> responseEntity = couponController.applyCoupon(request);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getData()).isInstanceOf(ApplyCouponResponse.class);
        assertThat(responseEntity.getBody().getData().getResponseMessage()).isEqualTo("이벤트에 참여하셨습니다.");
    }

    @Test
    @DisplayName("당첨자 확인 테스트")
    void checkWinner_이벤트당첨여부확인() {
        // Given
        Long eventId = 1L;
        Long userId = 1L;

        CheckWinnerResponse mockResponse = CheckWinnerResponse.builder().winningYn(true).build();
        when(couponService.checkWinner(any(CheckWinnerRequest.class))).thenReturn(mockResponse);

        // When
        ResponseEntity<ApiResponse<CheckWinnerResponse>> responseEntity = couponController.checkWinner(eventId, userId);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getData()).isInstanceOf(CheckWinnerResponse.class);
        assertThat(responseEntity.getBody().getData().isWinningYn()).isTrue();
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("축하합니다! 이벤트에 당첨되셨습니다.");
    }
}