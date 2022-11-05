package com.mimi.w2m.backend.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author : teddy
 * @since : 2022/09/30
 */
@Entity
@Getter
@Setter
@Table(name = "mimi_event_participable_time")
public class EventParticipableTime {
    @Id
    @Column(name = "event_participable_time_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "able_date")
    @Comment(value = "입력 날짜")
    private LocalDate ableDate;

    @Column(name = "start_time")
    @Comment(value = "날짜별 가능한 시작 시간")
    private LocalTime startTime;

    @Column(name = "end_time")
    @Comment(value = "날짜별 가능한 종료 시간")
    private LocalTime endTime;

    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", updatable = false, nullable = false)
    private Event event;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(targetEntity = Participant.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @Builder
    public EventParticipableTime(LocalDate ableDate, LocalTime startTime, LocalTime endTime, Event event, User user, Participant participant) {
        this.ableDate = ableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.event = event;
        this.user = user;
        this.participant = participant;
    }
    protected EventParticipableTime() {
    }
}
