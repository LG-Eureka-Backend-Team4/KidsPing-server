package com.kidsworld.kidsping.domain.event.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CheckWinnerRequest {

    private Long eventId;
    private Long userId;

}
