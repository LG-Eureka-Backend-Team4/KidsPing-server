package com.kidsworld.kidsping.domain.event.service;

import com.kidsworld.kidsping.domain.event.dto.request.CreateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.request.UpdateEventRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class EventCacheTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final int page = 0;
    private final int size = 10;

    @BeforeEach
    public void setUp() {
        // 이벤트 생성
        CreateEventRequest eventRequest = CreateEventRequest.builder()
                .eventName("Test Event")
                .eventContent("Test Event Content")
                .maxParticipants(100L)
                .announceTime(LocalDateTime.now().plusDays(1))
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusDays(2))
                .build();

        eventService.createEvent(eventRequest);

        // 캐시를 위해 getAllEvents 호출
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        eventService.getAllEvents(pageable);

        // 캐시에 데이터가 저장되었는지 확인
        String cacheKey = "event:list:page:" + page + ":size:" + size;
        Object cachedEvents = redisTemplate.opsForValue().get(cacheKey);
        assertThat(cachedEvents).isNotNull();
    }

    @Test
    @DisplayName("진행중인 이벤트 캐싱 테스트")
    void getAllEvents_캐시테스트() throws InterruptedException {
        // Given
        int page = 1;
        Pageable pageable = PageRequest.of(page, size);

        // When: 처음 호출하여 캐시 저장
        eventService.getAllEvents(pageable);

        // 캐시에 데이터가 저장되었는지 확인
        String cacheKey = "event:list:page:" + page + ":size:" + size;
        Object cachedEvents = redisTemplate.opsForValue().get(cacheKey);
        assertThat(cachedEvents).isNotNull();

        // When: 캐시 TTL이 만료될 때까지 대기
        TimeUnit.SECONDS.sleep(30); // 30초

        // Then: 캐시 만료 후 데이터가 없는지 확인
        cachedEvents = redisTemplate.opsForValue().get(cacheKey);
        assertThat(cachedEvents).isNull();
    }

    @Test
    @DisplayName("이벤트 생성 시 캐시 삭제 테스트")
    void createEvent_캐시삭제테스트() {
        // Given
        CreateEventRequest request = CreateEventRequest.builder()
                .eventName("New Event")
                .eventContent("New Content")
                .maxParticipants(100L)
                .announceTime(LocalDateTime.now().plusDays(1))
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusDays(2))
                .build();

        String cacheKey = "event:list:page:" + page + ":size:" + size;

        // When: 이벤트 생성
        eventService.createEvent(request);

        // Then: 캐시 삭제 확인
        Object cachedEvents = redisTemplate.opsForValue().get(cacheKey);
        assertThat(cachedEvents).isNull();
    }

    @Test
    @DisplayName("이벤트 업데이트 시 캐시 삭제 테스트")
    void updateEvent_캐시삭제테스트() {
        // Given
        UpdateEventRequest updateRequest = UpdateEventRequest.builder()
                .eventName("Updated Event")
                .eventContent("Updated Content")
                .maxParticipants(200L)
                .announceTime(LocalDateTime.now().plusDays(1))
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusDays(1))
                .build();

        String cacheKey = "event:list:page:" + page + ":size:" + size;

        // When: 이벤트 업데이트
        eventService.updateEvent(1L, updateRequest);

        // Then: 캐시 삭제 확인
        Object cachedEvents = redisTemplate.opsForValue().get(cacheKey);
        assertThat(cachedEvents).isNull();
    }

    @Test
    @DisplayName("이벤트 삭제 시 캐시 삭제 테스트")
    void deleteEvent_캐시삭제테스트() {
        // Given
        String cacheKey = "event:list:page:" + page + ":size:" + size;

        // When: 이벤트 삭제
        eventService.deleteEvent(1L);

        // Then: 캐시 삭제 확인
        Object cachedEvents = redisTemplate.opsForValue().get(cacheKey);
        assertThat(cachedEvents).isNull();
    }
}