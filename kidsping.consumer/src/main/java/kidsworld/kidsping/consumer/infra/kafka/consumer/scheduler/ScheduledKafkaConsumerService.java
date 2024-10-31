package kidsworld.kidsping.consumer.infra.kafka.consumer.scheduler;


import kidsworld.kidsping.consumer.domain.coupon.domain.Coupon;
import kidsworld.kidsping.consumer.domain.coupon.domain.Event;
import kidsworld.kidsping.consumer.domain.coupon.domain.User;
import kidsworld.kidsping.consumer.domain.coupon.repository.CouponRepository;
import kidsworld.kidsping.consumer.domain.coupon.repository.EventRepository;
import kidsworld.kidsping.consumer.domain.coupon.repository.UserRepository;
import kidsworld.kidsping.consumer.infra.kafka.consumer.event.CouponCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledKafkaConsumerService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;
    private final ConsumerFactory<String, CouponCreateEvent> consumerFactory;

    @Value("${spring.kafka.topic.coupon-create-event}")
    private String topicName;

    @Scheduled(cron = "0 0 4 * * *") // 매일 새벽 4시에 실행
    public void fetchAndStoreCouponCreateEvents() {
        try (Consumer<String, CouponCreateEvent> consumer = consumerFactory.createConsumer()) {
            consumer.subscribe(Collections.singletonList(topicName));

            ConsumerRecords<String, CouponCreateEvent> records = consumer.poll(Duration.ofSeconds(10));

            records.forEach(record -> {
                CouponCreateEvent couponCreateEvent = record.value();
                log.info("Processing event for user ID: {}, event ID: {}", couponCreateEvent.getUserId(), couponCreateEvent.getEventId());

                User user = userRepository.findById(couponCreateEvent.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                Event event = eventRepository.findById(couponCreateEvent.getEventId())
                        .orElseThrow(() -> new RuntimeException("Event not found"));

                Coupon coupon = Coupon.builder()
                        .user(user)
                        .event(event)
                        .name(couponCreateEvent.getName())
                        .phone(couponCreateEvent.getPhone())
                        .build();

                couponRepository.save(coupon);

                log.info("Coupon saved for user ID: {}", couponCreateEvent.getUserId());
            });
        } catch (RuntimeException e) {
            log.error("Failed to process fetchAndStoreCouponCreateEvents: {}", e.getMessage(), e);
        }
    }
}