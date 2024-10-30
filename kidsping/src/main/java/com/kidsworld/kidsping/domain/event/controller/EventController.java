package com.kidsworld.kidsping.domain.event.controller;

import com.kidsworld.kidsping.domain.event.dto.request.ApplyCouponRequest;
import com.kidsworld.kidsping.domain.event.dto.request.CreateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.request.CheckWinnerRequest;
import com.kidsworld.kidsping.domain.event.dto.request.UpdateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.response.*;
import com.kidsworld.kidsping.domain.event.dto.response.ApplyCouponResponse;
import com.kidsworld.kidsping.domain.event.dto.response.CreateEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.DeleteEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.GetEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.UpdateEventResponse;
import com.kidsworld.kidsping.domain.event.service.EventService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
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
    public ResponseEntity<ApiResponse<Page<GetEventResponse>>> getAllEvents(Pageable pageable) {
        Page<GetEventResponse> response = eventService.getAllEvents(pageable);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, ExceptionCode.OK.getMessage());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdateEventResponse>> updateEvent(
            @PathVariable Long id,
            @RequestBody UpdateEventRequest request) {

        UpdateEventResponse response = eventService.updateEvent(id, request);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, ExceptionCode.OK.getMessage());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<DeleteEventResponse>> deleteEvent(@PathVariable Long id) {
        DeleteEventResponse response = eventService.deleteEvent(id);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, ExceptionCode.OK.getMessage());
    }

    @PostMapping("/coupon")
    public ResponseEntity<ApiResponse<ApplyCouponResponse>> applyCoupon(
            @RequestBody ApplyCouponRequest applyCouponRequest) {
        eventService.applyCoupon(applyCouponRequest);
        return ApiResponse.ok(ExceptionCode.OK.getCode(), new ApplyCouponResponse("이벤트에 참여하셨습니다."),
                ExceptionCode.OK.getMessage());
    }

    @GetMapping("/check-winner")
    public ResponseEntity<ApiResponse<CheckWinnerResponse>> checkWinner(
            @RequestParam Long eventId,
            @RequestParam Long userId) {

        CheckWinnerRequest request = CheckWinnerRequest.builder().eventId(eventId).userId(userId).build();
        CheckWinnerResponse response = eventService.checkWinner(request);

        String message = response.isWinningYn() ? "축하합니다! 이벤트에 당첨되셨습니다." : "이벤트에 당첨되지 않았습니다.";

        return ApiResponse.ok(ExceptionCode.OK.getCode(), response, message);
    }

}
