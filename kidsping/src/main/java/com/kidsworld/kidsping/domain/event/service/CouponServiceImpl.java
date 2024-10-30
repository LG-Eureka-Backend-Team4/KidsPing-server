package com.kidsworld.kidsping.domain.event.service;

import com.kidsworld.kidsping.domain.event.dto.request.ApplyCouponRequest;
import com.kidsworld.kidsping.domain.event.dto.request.CheckWinnerRequest;
import com.kidsworld.kidsping.domain.event.dto.response.CheckWinnerResponse;
import com.kidsworld.kidsping.domain.event.entity.Coupon;
import com.kidsworld.kidsping.domain.event.repository.CouponRedisRepository;
import com.kidsworld.kidsping.domain.event.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRedisRepository couponRedisRepository;
    private final CouponRepository couponRepository;

    @Override
    public void applyCoupon(ApplyCouponRequest applyCouponRequest) {
        Long apply = couponRedisRepository.add(applyCouponRequest.getEventId(), applyCouponRequest.getUserId());

        if (apply != 1) {
            return;
        }

        Long count = couponRedisRepository.increment(applyCouponRequest.getEventId());

        if (count > 100) {
            return;
        }

        log.info("getUserId {}", applyCouponRequest.getUserId());
        log.info("getName {}", applyCouponRequest.getName());
//        couponCreateProducer.sendCouponCreateEvent(CouponCreateEvent.from(applyCouponRequest));
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
