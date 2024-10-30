package com.kidsworld.kidsping.domain.event.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyCouponRequest {

    private Long userId;
    private Long eventId;
    private String name;
    private String phone;
}
