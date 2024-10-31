package com.kidsworld.kidsping.domain.event.service;

import com.kidsworld.kidsping.domain.event.dto.request.ApplyCouponRequest;
import com.kidsworld.kidsping.domain.event.dto.request.CheckWinnerRequest;
import com.kidsworld.kidsping.domain.event.dto.response.CheckWinnerResponse;
import com.kidsworld.kidsping.domain.event.entity.Coupon;
import com.kidsworld.kidsping.domain.event.entity.Event;
import com.kidsworld.kidsping.domain.event.repository.CouponRedisRepository;
import com.kidsworld.kidsping.domain.event.repository.CouponRepository;
import com.kidsworld.kidsping.domain.event.repository.EventRepository;
import com.kidsworld.kidsping.domain.user.entity.User;
import com.kidsworld.kidsping.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

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

        User user = userRepository.findById(applyCouponRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("no event"));

        Event event = eventRepository.findById(applyCouponRequest.getEventId())
                .orElseThrow(() -> new RuntimeException("no event"));

        log.info("user.getId() {}", user.getId());
        log.info("event.getId() {}", event.getId());

        Coupon coupon = Coupon.builder()
                .user(user)
                .event(event)
                .name(applyCouponRequest.getName())
                .phone(applyCouponRequest.getPhone())
                .build();
        couponRepository.save(coupon);
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
