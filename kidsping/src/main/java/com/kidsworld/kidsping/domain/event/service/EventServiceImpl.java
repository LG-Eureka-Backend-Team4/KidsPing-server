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

    @Override
    public GetEventResponse getEvent(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException());

        return GetEventResponse.of(event);
    }

    @Override
    public Page<GetEventResponse> getAllEvents(Pageable pageable) {

        return eventRepository.findAll(pageable)
                .map(GetEventResponse::of);
    }

    @Override
    public UpdateEventResponse updateEvent(Long id, UpdateEventRequest updateEventRequest) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException());

        Event build = Event.builder()
                .id(event.getId())
                .eventName(updateEventRequest.getEventName())
                .eventContent(updateEventRequest.getEventContent())
                .maxParticipants(updateEventRequest.getMaxParticipants())
                .announceTime(updateEventRequest.getAnnounceTime())
                .startTime(updateEventRequest.getStartTime())
                .endTime(updateEventRequest.getEndTime())
                .build();

        Event updated = eventRepository.save(build);

        return UpdateEventResponse.of(updated);
    }

    @Override
    public DeleteEventResponse deleteEvent(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException());

        eventRepository.delete(event);

        return DeleteEventResponse.builder().id(id).build();
    }
}
