package com.kidsworld.kidsping.domain.event.service;

import com.kidsworld.kidsping.domain.event.dto.request.ApplyCouponRequest;
import com.kidsworld.kidsping.domain.event.dto.request.CheckWinnerRequest;
import com.kidsworld.kidsping.domain.event.dto.response.CheckWinnerResponse;

public interface CouponService {

    void applyCoupon(ApplyCouponRequest applyCouponRequest);

    String applyCouponAtomically(ApplyCouponRequest applyCouponRequest);

    CheckWinnerResponse checkWinner(CheckWinnerRequest request);

    CheckWinnerResponse isWinnerInCache(CheckWinnerRequest request);
}
