package com.kidsworld.kidsping.infra.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CouponCreateEvent {

    Long userId;
    Long eventId;
}
