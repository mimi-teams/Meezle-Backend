package com.mimi.w2m.backend.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author : teddy
 * @since : 2022/09/30
 */
@Getter
@Entity
@Table(name = "mimi_event_able_time")
public class EventAbleTime {
    @Id
    @Column(name = "event_able_time_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "able_date")
    private LocalDate ableDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", updatable = false)
    private Event event;

    @Builder
    public EventAbleTime(LocalDate ableDate, LocalTime startTime, LocalTime endTime, Event event) {
        this.ableDate = ableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.event = event;
    }

    protected EventAbleTime() {
    }

    public EventAbleTime update(LocalDate ableDate, LocalTime startTime, LocalTime endTime) {
        this.ableDate = ableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        return this;
    }
}