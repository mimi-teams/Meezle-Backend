package com.mimi.w2m.backend.domain;

import com.mimi.w2m.backend.domain.converter.SetDayOfWeekConverter;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

/**
 * EventParticipableTime
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/16
 **/
@Entity
@Getter
@Table(name = "meezle_event_participable_time")
public class EventParticipableTime {
@Id
@Column(name = "event_participable_time_id")
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Comment(value = "각 참여자가 입력한 자신이 가능한 요일")
@Convert(converter = SetDayOfWeekConverter.class)
@Column(name = "able_date", columnDefinition = "VARCHAR(100)")
private Set<DayOfWeek> ableDayOfWeeks;

@Comment(value = "각 참여자가 입력한 날짜별 가능한 시작 시간")
@Column(name = "start_time")
private LocalTime startTime;

@Comment(value = "각 참여자가 입력한 날짜별 가능한 종료 시간")
@Column(name = "end_time")
private LocalTime endTime;

@Comment("연관된 event")
@ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "event_id", updatable = false, nullable = false)
private Event event;

@Comment("참여자의 정보(user)")
@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
private User user;

@Comment("참여자의 정보(user)")
@ManyToOne(targetEntity = Participant.class, fetch = FetchType.LAZY)
@JoinColumn(name = "participant_id")
private Participant participant;

protected EventParticipableTime() {
}

@Builder
public EventParticipableTime(Set<DayOfWeek> ableDayOfWeeks, LocalTime startTime, LocalTime endTime, Event event,
                             User user, Participant participant) {
    this.ableDayOfWeeks = ableDayOfWeeks;
    this.startTime      = startTime;
    this.endTime        = endTime;
    this.event          = event;
    this.user           = user;
    this.participant    = participant;
}

EventParticipableTime update(Set<DayOfWeek> ableDayOfWeeks, LocalTime startTime, LocalTime endTime) {
    this.ableDayOfWeeks = ableDayOfWeeks;
    this.startTime      = startTime;
    this.endTime        = endTime;
    return this;
}
}
