package com.kidsworld.kidsping.domain.event.controller;

import com.kidsworld.kidsping.domain.event.dto.request.CreateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.request.UpdateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.response.CreateEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.DeleteEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.GetEventResponse;
import com.kidsworld.kidsping.domain.event.dto.response.UpdateEventResponse;
import com.kidsworld.kidsping.domain.event.entity.Event;
import com.kidsworld.kidsping.domain.event.service.EventService;
import com.kidsworld.kidsping.global.common.dto.ApiResponse;
import com.kidsworld.kidsping.global.exception.ExceptionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    private Event mockEvent;

    @BeforeEach
    void setUp() {
        mockEvent = Event.builder()
                .id(1L)
                .eventName("Mock Event")
                .eventContent("Mock Content")
                .maxParticipants(100L)
                .announceTime(LocalDateTime.now())
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(2))
                .build();
    }

    @Test
    @DisplayName("이벤트 생성 테스트")
    void createEvent_이벤트생성() {
        // Given
        CreateEventRequest request = CreateEventRequest.builder()
                .eventName("Mock Event")
                .eventContent("Mock Content")
                .maxParticipants(100L)
                .announceTime(LocalDateTime.now())
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(2))
                .build();

        CreateEventResponse expectedResponse = CreateEventResponse.of(mockEvent);
        when(eventService.createEvent(any(CreateEventRequest.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponse<CreateEventResponse>> responseEntity = eventController.createEvent(request);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getData()).isEqualTo(expectedResponse);
        assertThat(responseEntity.getBody().getMessage()).isEqualTo(ExceptionCode.OK.getMessage());
    }

    @Test
    @DisplayName("이벤트 조회 테스트")
    void getEvent_이벤트조회() {
        // Given
        GetEventResponse expectedResponse = GetEventResponse.of(mockEvent);
        when(eventService.getEvent(any(Long.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponse<GetEventResponse>> responseEntity = eventController.getEvent(mockEvent.getId());

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getData()).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("진행중인 이벤트 조회 테스트")
    void getAllEvents_진행중인이벤트조회() {
        // Given
        Pageable pageable = Pageable.unpaged();
        Page<GetEventResponse> expectedResponse = mock(Page.class);
        when(eventService.getAllEvents(pageable)).thenReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponse<Page<GetEventResponse>>> responseEntity = eventController.getAllEvents(pageable);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getData()).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("이벤트 업데이트 테스트")
    void updateEvent_이벤트업데이트() {
        // Given
        UpdateEventRequest updateRequest = UpdateEventRequest.builder()
                .eventName("Updated Event")
                .eventContent("Updated Content")
                .maxParticipants(200L)
                .announceTime(LocalDateTime.now())
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusDays(1))
                .build();

        Event updatedEvent = Event.builder()
                .id(mockEvent.getId())
                .eventName(updateRequest.getEventName())
                .eventContent(updateRequest.getEventContent())
                .maxParticipants(updateRequest.getMaxParticipants())
                .announceTime(updateRequest.getAnnounceTime())
                .startTime(updateRequest.getStartTime())
                .endTime(updateRequest.getEndTime())
                .build();

        UpdateEventResponse expectedResponse = UpdateEventResponse.of(updatedEvent);

        when(eventService.updateEvent(any(Long.class), any(UpdateEventRequest.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponse<UpdateEventResponse>> responseEntity = eventController.updateEvent(mockEvent.getId(), updateRequest);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getData()).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("이벤트 삭제 테스트")
    void deleteEvent_이벤트삭제() {
        // Given
        DeleteEventResponse expectedResponse = DeleteEventResponse.builder().id(mockEvent.getId()).build();
        when(eventService.deleteEvent(any(Long.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponse<DeleteEventResponse>> responseEntity = eventController.deleteEvent(mockEvent.getId());

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getData()).isEqualTo(expectedResponse);
    }
}