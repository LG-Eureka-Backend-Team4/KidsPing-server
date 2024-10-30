package com.kidsworld.kidsping.domain.event.service;

import com.kidsworld.kidsping.domain.event.dto.request.ApplyCouponRequest;
import com.kidsworld.kidsping.domain.event.dto.request.CreateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.request.CheckWinnerRequest;
import com.kidsworld.kidsping.domain.event.dto.request.UpdateEventRequest;
import com.kidsworld.kidsping.domain.event.dto.response.*;
import com.kidsworld.kidsping.domain.event.entity.Coupon;
import com.kidsworld.kidsping.domain.event.entity.Event;
import com.kidsworld.kidsping.domain.event.exception.EventNotFoundException;
import com.kidsworld.kidsping.domain.event.repository.CouponRepository;
import com.kidsworld.kidsping.domain.event.repository.EventRepository;
import com.kidsworld.kidsping.domain.event.repository.EventWinnerRepository;
import com.kidsworld.kidsping.infra.kafka.CouponCreateProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CouponRepository couponRepository;
    private final CouponCreateProducer couponCreateProducer;
    private final EventWinnerRepository eventWinnerRepository;

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

    @Override
    public void applyCoupon(ApplyCouponRequest applyCouponRequest) {
        Long apply = couponRepository.add(applyCouponRequest.getEventId(), applyCouponRequest.getUserId());

        if (apply != 1) {
            return;
        }

        Long count = couponRepository.increment(applyCouponRequest.getEventId());

        if (count > 100) {
            return;
        }

        log.info("getUserId {}", applyCouponRequest.getUserId());
        log.info("getName {}", applyCouponRequest.getName());
//        couponCreateProducer.sendCouponCreateEvent(CouponCreateEvent.from(applyCouponRequest));
    }

    @Override
    public CheckWinnerResponse checkWinner(CheckWinnerRequest request) {
        Long userId = request.getUserId();
        Long eventId = request.getEventId();

        Optional<Coupon> winner = eventWinnerRepository.findByUserIdAndEventId(userId, eventId);

        if (winner.isPresent()) {
            return CheckWinnerResponse.of(true);
        } else {
            return CheckWinnerResponse.of(false);
        }
    }
}
