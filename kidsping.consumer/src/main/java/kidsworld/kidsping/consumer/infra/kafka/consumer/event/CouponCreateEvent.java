package kidsworld.kidsping.consumer.infra.kafka.consumer.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponCreateEvent {

    Long userId;
    Long eventId;
}
