package com.kidsworld.kidsping.infra.kafka.event;

import com.kidsworld.kidsping.domain.event.dto.request.ApplyCouponRequest;
import lombok.Builder;
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

    @Builder
    private CouponCreateEvent(Long userId, Long eventId, String name, String phone) {
        this.userId = userId;
        this.eventId = eventId;
        this.name = name;
        this.phone = phone;
    }

    public static CouponCreateEvent from(ApplyCouponRequest applyCouponRequest) {
        return CouponCreateEvent.builder()
                .userId(applyCouponRequest.getUserId())
                .eventId(applyCouponRequest.getEventId())
                .name(applyCouponRequest.getName())
                .phone(applyCouponRequest.getPhone())
                .build();
    }
}
