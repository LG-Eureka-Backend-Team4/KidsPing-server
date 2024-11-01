package com.kidsworld.kidsping.domain.event.repository;

import com.kidsworld.kidsping.domain.event.entity.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventRepositoryTest {

    @Mock
    private EventRepository eventRepository;

    @Test
    void findOngoingEvents_진행중인이벤트반환() {
        // Given
        LocalDateTime currentTime = LocalDateTime.now();
        Pageable pageable = Pageable.unpaged();

        Event ongoingEvent = Event.builder()
                .id(1L)
                .eventName("진행 중 이벤트")
                .eventContent("이 이벤트는 현재 진행 중입니다.")
                .maxParticipants(100L)
                .announceTime(LocalDateTime.now().minusDays(1))
                .startTime(LocalDateTime.now().minusHours(1))
                .endTime(LocalDateTime.now().plusHours(1))
                .build();

        when(eventRepository.findOngoingEvents(currentTime, pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(ongoingEvent)));

        // When
        Page<Event> result = eventRepository.findOngoingEvents(currentTime, pageable);

        // Then
        assertEquals(1, result.getContent().size());
        assertEquals(ongoingEvent.getId(), result.getContent().get(0).getId());
    }

    @Test
    void findOngoingEvents_진행중인이벤트가없을때빈리스트반환() {
        // Given
        LocalDateTime currentTime = LocalDateTime.now();
        Pageable pageable = Pageable.unpaged();

        when(eventRepository.findOngoingEvents(currentTime, pageable))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        // When
        Page<Event> result = eventRepository.findOngoingEvents(currentTime, pageable);

        // Then
        assertTrue(result.getContent().isEmpty());
    }
}