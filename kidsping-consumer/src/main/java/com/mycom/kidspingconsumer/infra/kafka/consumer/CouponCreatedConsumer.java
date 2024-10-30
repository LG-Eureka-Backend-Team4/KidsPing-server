package com.mycom.kidspingconsumer.infra.kafka.consumer;

import com.mycom.kidspingconsumer.domain.coupon.entity.Coupon;
import com.mycom.kidspingconsumer.domain.coupon.entity.Event;
import com.mycom.kidspingconsumer.domain.coupon.entity.User;
import com.mycom.kidspingconsumer.domain.coupon.repository.CouponRepository;
import com.mycom.kidspingconsumer.domain.coupon.repository.EventRepository;
import com.mycom.kidspingconsumer.domain.coupon.repository.UserRepository;
import com.mycom.kidspingconsumer.infra.kafka.consumer.event.CouponCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponCreatedConsumer {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;

    @KafkaListener(topics = "${spring.kafka.topic.coupon-create-event}", groupId = "${spring.kafka.consumer.group-id.coupon-create-group}", containerFactory = "couponCreateEventListenerContainerFactory")
    public void listener(CouponCreateEvent couponCreateEvent) {

        log.info("couponCreateEvent.getUserId() {}",  couponCreateEvent.getUserId());
        log.info("couponCreateEvent.getEventId() {}", couponCreateEvent.getEventId());
        User user = userRepository.findById(couponCreateEvent.getUserId())
                .orElseThrow(() -> new RuntimeException("no event"));

        Event event = eventRepository.findById(couponCreateEvent.getEventId())
                .orElseThrow(() -> new RuntimeException("no event"));

        Coupon coupon = Coupon.builder()
                .user(user)
                .event(event)
                .name(couponCreateEvent.getName())
                .phone(couponCreateEvent.getPhone())
                .build();

        couponRepository.save(coupon);
    }
}