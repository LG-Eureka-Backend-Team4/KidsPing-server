package com.kidsworld.kidsping.domain.event.dto.response;

import com.kidsworld.kidsping.domain.event.entity.Event;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CreateEventResponse {

    private Long id;
    private String eventName;
    private LocalDateTime createdAt;

    public static CreateEventResponse of(Event event) {
        return CreateEventResponse.builder()
                .id(event.getId())
                .eventName(event.getEventName())
                .createdAt(event.getCreatedAt())
                .build();
    }
}
