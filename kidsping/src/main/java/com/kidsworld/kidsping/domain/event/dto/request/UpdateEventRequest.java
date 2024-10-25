package com.kidsworld.kidsping.domain.event.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UpdateEventRequest {
    private String eventName;
    private String eventContent;
    private Long maxParticipants;
    private LocalDateTime announceTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}