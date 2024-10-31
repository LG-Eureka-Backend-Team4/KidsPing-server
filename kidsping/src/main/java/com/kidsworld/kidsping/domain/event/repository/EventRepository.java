package com.kidsworld.kidsping.domain.event.repository;

import com.kidsworld.kidsping.domain.event.entity.Event;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends JpaRepository<Event,Long>  {
    @Query("SELECT e FROM Event e WHERE e.startTime <= :currentTime AND e.endTime >= :currentTime")
    Page<Event> findOngoingEvents(@Param("currentTime") LocalDateTime currentTime, Pageable pageable);
}
