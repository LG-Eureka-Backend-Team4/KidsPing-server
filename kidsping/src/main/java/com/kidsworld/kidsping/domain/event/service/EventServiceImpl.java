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
import com.kidsworld.kidsping.global.cache.CachedPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    @CacheEvict(value = "eventPagesCache", allEntries = true)
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
    @Cacheable(value = "eventPagesCache", key = "'events:page:' + #p0 + ':size:' + #p1")
    public CachedPage<GetEventResponse> getAllEvents(int page, int size) {
        log.info("check page and size value! page: {}, size: {}", page, size);

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Event> eventPage = eventRepository.findOngoingEvents(LocalDateTime.now(), pageRequest);

        List<GetEventResponse> eventResponses = eventPage
                .map(GetEventResponse::of)
                .getContent();

        return new CachedPage<>(eventResponses, eventPage.getNumber(), eventPage.getSize(), eventPage.getTotalElements());
    }

    @Override
    @CacheEvict(value = "eventPagesCache", allEntries = true)
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
    @CacheEvict(value = "eventPagesCache", allEntries = true)
    public DeleteEventResponse deleteEvent(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException());

        eventRepository.delete(event);

        return DeleteEventResponse.builder().id(id).build();
    }
}