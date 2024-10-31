package com.kidsworld.kidsping.domain.event.controller;

import com.kidsworld.kidsping.domain.event.dto.request.ApplyCouponRequest;
import com.kidsworld.kidsping.domain.event.dto.request.CheckWinnerRequest;
import com.kidsworld.kidsping.domain.event.dto.response.ApplyCouponResponse;
import com.kidsworld.kidsping.domain.event.dto.response.CheckWinnerResponse;
import com.kidsworld.kidsping.domain.event.service.CouponService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<ApplyCouponResponse>> applyCoupon(
            @RequestBody ApplyCouponRequest applyCouponRequest) {
        couponService.applyCoupon(applyCouponRequest);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), new ApplyCouponResponse("이벤트에 참여하셨습니다."),
                ExceptionCode.OK.getMessage());
    }

    @GetMapping("/winners/check")
    public ResponseEntity<ApiResponse<CheckWinnerResponse>> checkWinner(
            @RequestParam Long eventId,
            @RequestParam Long userId) {

        CheckWinnerRequest request = CheckWinnerRequest.builder().eventId(eventId).userId(userId).build();
        CheckWinnerResponse response = couponService.checkWinner(request);

        String message = response.isWinningYn() ? "축하합니다! 이벤트에 당첨되셨습니다." : "이벤트에 당첨되지 않았습니다.";

        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, message);
    }
}