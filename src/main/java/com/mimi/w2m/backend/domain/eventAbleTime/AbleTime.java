package com.mimi.w2m.backend.domain.eventAbleTime;

import com.mimi.w2m.backend.domain.event.Event;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author : teddy
 * @since : 2022/09/30
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "w2m_event_able_time")
public class AbleTime {
    @Id
    @Column(name = "event_able_time_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "able_date", nullable = false)
    LocalDateTime ableDate;

    @Column(name = "start_time")
    LocalDateTime startTime;

    @Column(name = "end_time")
    LocalDateTime endTime;

    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", unique = true, updatable = false)
    private Event event;

    @Builder
    public AbleTime(LocalDateTime ableDate, LocalDateTime startTime, LocalDateTime endTime, Event event) {
        this.ableDate = ableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.event = event;
    }

    public AbleTime update(LocalDateTime ableDate, LocalDateTime startTime, LocalDateTime endTime) {
        this.ableDate = ableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        return this;
    }
}