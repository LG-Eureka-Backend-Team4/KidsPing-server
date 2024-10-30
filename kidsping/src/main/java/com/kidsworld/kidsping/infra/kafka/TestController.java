package com.kidsworld.kidsping.infra.kafka;

import com.kidsworld.kidsping.infra.kafka.event.CouponCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final CouponCreateProducer couponCreateProducer;

    @PostMapping("/coupon/test")
    public void sendCoupon(@RequestBody CouponCreateEvent couponCreateEvent) {
        log.info("getUserId {}", couponCreateEvent.getUserId());
        log.info("getEventId {}", couponCreateEvent.getEventId());
        couponCreateProducer.setCouponCreateEvent(couponCreateEvent);
    }
}
