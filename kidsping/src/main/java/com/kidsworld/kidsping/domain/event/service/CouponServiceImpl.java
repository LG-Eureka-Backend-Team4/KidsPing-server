package com.kidsworld.kidsping.domain.event.service;

import com.kidsworld.kidsping.domain.event.dto.request.ApplyCouponRequest;
import com.kidsworld.kidsping.domain.event.dto.request.CheckWinnerRequest;
import com.kidsworld.kidsping.domain.event.dto.response.CheckWinnerResponse;
import com.kidsworld.kidsping.domain.event.entity.Coupon;
import com.kidsworld.kidsping.domain.event.repository.CouponRedisRepository;
import com.kidsworld.kidsping.domain.event.repository.CouponRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRedisRepository couponRedisRepository;
    private final CouponRepository couponRepository;

    @Override
    public void applyCoupon(ApplyCouponRequest applyCouponRequest) {
        Long isAlreadyApplied = couponRedisRepository.verifyParticipation(applyCouponRequest.getEventId(),
                applyCouponRequest.getUserId());

        if (isAlreadyApplied != 1) {
            return;
        }

        Long currentCouponCount = couponRedisRepository.incrementIssuedCouponCount(applyCouponRequest.getEventId());

        if (currentCouponCount > 100) {
            return;
        }

        couponRedisRepository.saveApplyCoupon(applyCouponRequest);
    }

    @Override
    public CheckWinnerResponse checkWinner(CheckWinnerRequest request) {
        Long userId = request.getUserId();
        Long eventId = request.getEventId();

        Optional<Coupon> winner = couponRepository.findByUserIdAndEventId(userId, eventId);

        if (winner.isPresent()) {
            return CheckWinnerResponse.of(true);
        } else {
            return CheckWinnerResponse.of(false);
        }
    }

}
