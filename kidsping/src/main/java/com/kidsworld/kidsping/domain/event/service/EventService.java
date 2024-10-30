package com.kidsworld.kidsping.domain.event.service;

import com.kidsworld.kidsping.domain.event.dto.request.ApplyCouponRequest;
import com.kidsworld.kidsping.domain.event.dto.request.CreateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.request.UpdateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.response.CreateEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.DeleteEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.GetEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.UpdateEventResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface EventService {

    CreateEventResponse createEvent(CreateEventRequest createEventRequest);

    GetEventResponse getEvent(Long id);

    Page<GetEventResponse> getAllEvents(Pageable pageable);

    UpdateEventResponse updateEvent(Long id, UpdateEventRequest updateEventRequest);

    DeleteEventResponse deleteEvent(Long id);

    void applyCoupon(ApplyCouponRequest applyCouponRequest);
}
