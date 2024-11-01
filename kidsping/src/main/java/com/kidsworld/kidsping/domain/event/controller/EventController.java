package com.kidsworld.kidsping.domain.event.controller;

import com.kidsworld.kidsping.domain.event.dto.request.CreateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.request.UpdateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.response.CreateEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.DeleteEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.GetEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.UpdateEventResponse;
import com.kidsworld.kidsping.domain.event.service.EventService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    @CacheEvict(value = "eventPagesCache", allEntries = true)
    public ResponseEntity<ApiResponse<CreateEventResponse>> createEvent(@RequestBody CreateEventRequest request) {
        CreateEventResponse response = eventService.createEvent(request);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, ExceptionCode.OK.getMessage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetEventResponse>> getEvent(@PathVariable Long id) {
        GetEventResponse response = eventService.getEvent(id);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, ExceptionCode.OK.getMessage());
    }

    @GetMapping
    @Cacheable(value = "eventPagesCache", key = "#pageable.pageNumber")
    public ResponseEntity<ApiResponse<Page<GetEventResponse>>> getAllEvents(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<GetEventResponse> response = eventService.getAllEvents(pageRequest);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, ExceptionCode.OK.getMessage());
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "eventPagesCache", allEntries = true)
    public ResponseEntity<ApiResponse<UpdateEventResponse>> updateEvent(
            @PathVariable Long id,
            @RequestBody UpdateEventRequest request) {

        UpdateEventResponse response = eventService.updateEvent(id, request);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, ExceptionCode.OK.getMessage());
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "eventPagesCache", allEntries = true)
    public ResponseEntity<ApiResponse<DeleteEventResponse>> deleteEvent(@PathVariable Long id) {
        DeleteEventResponse response = eventService.deleteEvent(id);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, ExceptionCode.OK.getMessage());
    }
}