package com.kidsworld.kidsping.domain.event.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyCouponRequest {

    private Long userId;
    private Long eventId;
    private String name;
    private String phone;

    @Builder
    private ApplyCouponRequest(Long userId, Long eventId, String name, String phone) {
        this.userId = userId;
        this.eventId = eventId;
        this.name = name;
        this.phone = phone;
    }
}
