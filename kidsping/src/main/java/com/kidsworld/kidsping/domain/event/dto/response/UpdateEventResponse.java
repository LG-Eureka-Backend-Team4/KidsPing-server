package com.kidsworld.kidsping.domain.event.dto.response;

import com.kidsworld.kidsping.domain.event.dto.request.UpdateEventRequest;
import com.kidsworld.kidsping.domain.event.entity.Event;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class UpdateEventResponse {

    private Long id;
    private String eventName;
    private LocalDateTime updatedAt;

    public static UpdateEventResponse of(Event event) {
        return UpdateEventResponse.builder()
                .id(event.getId())
                .eventName(event.getEventName())
                .updatedAt(event.getUpdatedAt())
                .build();
    }

}
