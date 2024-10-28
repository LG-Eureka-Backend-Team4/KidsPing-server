package com.kidsworld.kidsping.domain.event.dto.response;

import com.kidsworld.kidsping.domain.event.entity.Event;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class GetEventResponse {

    private Long id;
    private String eventName;
    private String eventContent;
    private Long maxParticipants;
    private LocalDateTime announceTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static GetEventResponse of(Event event) {
        return GetEventResponse.builder()
                .id(event.getId())
                .eventName(event.getEventName())
                .eventContent(event.getEventContent())
                .maxParticipants(event.getMaxParticipants())
                .announceTime(event.getAnnounceTime())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }

}
