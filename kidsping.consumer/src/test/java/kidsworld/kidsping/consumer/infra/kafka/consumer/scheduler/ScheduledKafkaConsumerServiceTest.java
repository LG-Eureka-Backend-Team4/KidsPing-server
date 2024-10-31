package com.mycom.kidspingconsumer.infra.kafka.consumer;

import com.mycom.kidspingconsumer.domain.coupon.entity.Event;
import com.mycom.kidspingconsumer.domain.coupon.entity.User;
import com.mycom.kidspingconsumer.domain.coupon.repository.CouponRepository;
import com.mycom.kidspingconsumer.domain.coupon.repository.EventRepository;
import com.mycom.kidspingconsumer.domain.coupon.repository.UserRepository;
import com.mycom.kidspingconsumer.infra.kafka.consumer.event.CouponCreateEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "couponCreateEvent" })
@Transactional
public class ScheduledKafkaConsumerServiceTest {

    private KafkaTemplate<String, CouponCreateEvent> kafkaTemplate;

    @Autowired
    private ScheduledKafkaConsumerService scheduledKafkaConsumerService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    private Long testUserId;
    private Long testEventId;

    @BeforeEach
    public void setup() {
        // Kafka Producer 설정
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getProperty("spring.kafka.bootstrap-servers"));
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerProps));

        User user = User.builder().userName("testUser").build();
        user = userRepository.save(user);

        Event event = Event.builder().eventName("testEvent").build();
        event = eventRepository.save(event);

        this.testUserId = user.getId();
        this.testEventId = event.getId();
    }

    @Test
    public void testFetchAndStoreCouponCreateEvents() throws Exception {
        IntStream.range(0, 100).forEach(i -> {
            CouponCreateEvent testEvent = new CouponCreateEvent();
            testEvent.setUserId(testUserId);
            testEvent.setEventId(testEventId);
            testEvent.setName("User " + i);
            testEvent.setPhone("01012341234");

            try {
                kafkaTemplate.send("couponCreateEvent", testEvent);
            } catch (Exception e) {
                throw new RuntimeException("Kafka 메시지 전송 중 오류 발생", e);
            }
        });

        scheduledKafkaConsumerService.fetchAndStoreCouponCreateEvents();

        Thread.sleep(5000);

        assertThat(couponRepository.countByEventId(testEventId)).isEqualTo(100);
    }
}
