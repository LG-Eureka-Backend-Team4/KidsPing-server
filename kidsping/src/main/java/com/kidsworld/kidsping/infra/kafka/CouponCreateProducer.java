package com.kidsworld.kidsping.infra.kafka;

import com.kidsworld.kidsping.infra.kafka.event.CouponCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponCreateProducer {

    @Value("${spring.kafka.topic.coupon-create-event}")
    private String couponCreateEventTopic;

    private final KafkaTemplate<String, CouponCreateEvent> kafkaTemplate;

    public void sendCouponCreateEvent(CouponCreateEvent couponCreateEvent) {
        kafkaTemplate.send(couponCreateEventTopic, couponCreateEvent);
    }
}