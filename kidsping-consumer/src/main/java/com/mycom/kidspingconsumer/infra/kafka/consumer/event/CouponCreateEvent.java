package com.mycom.kidspingconsumer.infra.kafka.consumer.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CouponCreateEvent {

    Long userId;
    Long eventId;
    private String name;
    private String phone;
}