package com.kidsworld.kidsping.domain.event.service;

import com.kidsworld.kidsping.domain.event.dto.request.CreateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.request.UpdateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.response.CreateEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.DeleteEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.GetEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.UpdateEventResponse;
import com.kidsworld.kidsping.domain.event.entity.Event;
import com.kidsworld.kidsping.domain.event.exception.EventNotFoundException;
import com.kidsworld.kidsping.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public CreateEventResponse createEvent(CreateEventRequest createEventRequest) {

        Event event = Event.builder()
                .eventName(createEventRequest.getEventName())
                .eventContent(createEventRequest.getEventContent())
                .maxParticipants(createEventRequest.getMaxParticipants())
                .announceTime(createEventRequest.getAnnounceTime())
                .startTime(createEventRequest.getStartTime())
                .endTime(createEventRequest.getEndTime())
                .build();

        Event saved = eventRepository.save(event);

        return CreateEventResponse.of(saved);
    }

}
