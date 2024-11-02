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
    public String applyCouponAtomically(ApplyCouponRequest applyCouponRequest) {
        Long applyStatus = couponRedisRepository.applyCouponAtomically(applyCouponRequest);
        if (applyStatus == 1) {
            log.info("쿠폰 발급 성공 - 사용자 ID {}", applyCouponRequest.getUserId());
        } else if (applyStatus == 0) {
            log.warn("사용자 {}는 이미 이벤트에 참여했습니다.", applyCouponRequest.getUserId());
            return " 이미 이벤트에 참여했습니다.";
        } else if (applyStatus == -1) {
            log.warn("이벤트 ID {}의 쿠폰 한도가 초과되었습니다.", applyCouponRequest.getEventId());
        } else {
            log.error("쿠폰 발급 중 알 수 없는 오류 발생 - 사용자 ID {}", applyCouponRequest.getUserId());
        }
        return "이벤트에 참여하셨습니다.";
    }

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
