package com.kidsworld.kidsping.domain.event.service;

import com.kidsworld.kidsping.domain.event.dto.request.CreateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.request.UpdateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.response.*;
import com.kidsworld.kidsping.global.cache.CachedPage;


public interface EventService {

    CreateEventResponse createEvent(CreateEventRequest createEventRequest);

    GetEventResponse getEvent(Long id);

    CachedPage<GetEventResponse> getAllEvents(int page, int size);

    UpdateEventResponse updateEvent(Long id, UpdateEventRequest updateEventRequest);

    DeleteEventResponse deleteEvent(Long id);
}
