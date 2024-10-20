package com.kidsworld.kidsping.domain.event.entity;


import com.kidsworld.kidsping.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Event extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    private String eventName;
    private String eventContent;
    private Long maxParticipants;
    private LocalDateTime announceTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "event", cascade = CascadeType.PERSIST)
    private List<EventParticipant> eventParticipants = new ArrayList<>();

}