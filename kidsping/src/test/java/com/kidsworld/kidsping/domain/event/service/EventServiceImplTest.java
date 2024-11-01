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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @InjectMocks
    private EventServiceImpl eventService;

    @Mock
    private EventRepository eventRepository;

    private Event mockEvent;

    @BeforeEach
    void setUp() {
        mockEvent = Event.builder()
                .id(1L)
                .eventName("New Event")
                .eventContent("New Event Content")
                .maxParticipants(50L)
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
                .eventName("New Event")
                .eventContent("New Event Content")
                .maxParticipants(50L)
                .announceTime(LocalDateTime.now())
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(2))
                .build();

        when(eventRepository.save(any(Event.class))).thenReturn(mockEvent);

        // When
        CreateEventResponse response = eventService.createEvent(request);

        // Then
        assertThat(response.getId()).isEqualTo(mockEvent.getId());
        assertThat(response.getEventName()).isEqualTo(mockEvent.getEventName());
    }

    @Test
    @DisplayName("이벤트 조회 테스트")
    void getEvent_이벤트조회() {
        // Given
        when(eventRepository.findById(mockEvent.getId())).thenReturn(Optional.of(mockEvent));

        // When
        GetEventResponse response = eventService.getEvent(mockEvent.getId());

        // Then
        assertThat(response.getId()).isEqualTo(mockEvent.getId());
        assertThat(response.getEventName()).isEqualTo(mockEvent.getEventName());
    }

    @Test
    @DisplayName("존재하지 않는 이벤트 조회 시 예외 발생")
    void getEvent_이벤트존재하지않음() {
        // Given
        when(eventRepository.findById(mockEvent.getId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EventNotFoundException.class, () -> eventService.getEvent(mockEvent.getId()));
    }

    void updateEvent_이벤트업데이트() {
        // Given
        UpdateEventRequest updateRequest = UpdateEventRequest.builder()
                .eventName("Updated Event")
                .eventContent("Updated content")
                .maxParticipants(200L)
                .announceTime(LocalDateTime.now())
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusDays(1))
                .build();

        Event updatedMockEvent = Event.builder()
                .id(mockEvent.getId())
                .eventName("Updated Event")
                .eventContent("Updated content")
                .maxParticipants(200L)
                .announceTime(LocalDateTime.now())
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusDays(1))
                .build();

        when(eventRepository.findById(mockEvent.getId())).thenReturn(Optional.of(mockEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedMockEvent);

        // When
        UpdateEventResponse response = eventService.updateEvent(mockEvent.getId(), updateRequest);

        // Then
        assertThat(response.getId()).isEqualTo(updatedMockEvent.getId());
        assertThat(response.getEventName()).isEqualTo(updateRequest.getEventName()); // Check for updated value
    }

    @Test
    @DisplayName("존재하지 않는 이벤트 업데이트 시 예외 발생")
    void updateEvent_이벤트존재하지않음() {
        // Given
        UpdateEventRequest updateRequest = UpdateEventRequest.builder().build();

        when(eventRepository.findById(mockEvent.getId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EventNotFoundException.class, () -> eventService.updateEvent(mockEvent.getId(), updateRequest));
    }

    @Test
    @DisplayName("이벤트 삭제 테스트")
    void deleteEvent_이벤트삭제() {
        // Given
        when(eventRepository.findById(mockEvent.getId())).thenReturn(Optional.of(mockEvent));

        // When
        DeleteEventResponse response = eventService.deleteEvent(mockEvent.getId());

        // Then
        assertThat(response.getId()).isEqualTo(mockEvent.getId());
        verify(eventRepository, times(1)).delete(mockEvent);
    }

    @Test
    @DisplayName("존재하지 않는 이벤트 삭제 시 예외 발생")
    void deleteEvent_이벤트존재하지않음() {
        // Given
        when(eventRepository.findById(mockEvent.getId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EventNotFoundException.class, () -> eventService.deleteEvent(mockEvent.getId()));
    }
}