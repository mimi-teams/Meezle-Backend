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
@Entity
@Getter
@Table(name = "w2m_event_able_time")
public class AbleTime {
    @Id
    @Column(name = "event_able_time_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "able_date")
    LocalDate ableDate;

    @Column(name = "start_time")
    LocalTime startTime;

    @Column(name = "end_time")
    LocalTime endTime;

    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", updatable = false)
    private Event event;

    @Builder
    public AbleTime(LocalDate ableDate, LocalTime startTime, LocalTime endTime, Event event) {
        this.ableDate = ableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.event = event;
    }

    protected AbleTime() {
    }

    public AbleTime update(LocalDate ableDate, LocalTime startTime, LocalTime endTime) {
        this.ableDate = ableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        return this;
    }
}