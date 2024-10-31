package kidsworld.kidsping.consumer.global.config;

import java.util.HashMap;
import java.util.Map;
import kidsworld.kidsping.consumer.infra.kafka.consumer.event.CouponCreateEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id.coupon-create-group}")
    private String groupId;

    @Bean
    public Map<String, Object> couponCreateEventConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return config;
    }

    @Bean
    public ConsumerFactory<String, CouponCreateEvent> couponCreateEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                couponCreateEventConfig(),
                new StringDeserializer(),
                new JsonDeserializer<>(CouponCreateEvent.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CouponCreateEvent> couponCreateEventListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CouponCreateEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(couponCreateEventConsumerFactory());
        return factory;
    }
}