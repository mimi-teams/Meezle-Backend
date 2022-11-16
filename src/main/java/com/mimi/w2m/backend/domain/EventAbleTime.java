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
 * EventAbleTime
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/16
 **/
@Entity
@Getter
@Table(name = "meezle_event_able_time")
public class EventAbleTime {
@Id
@Column(name = "event_able_time_id")
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Comment("event_participable_time에서 모두가 가능한 요일")
@Convert(converter = SetDayOfWeekConverter.class)
@Column(name = "able_day_of_weeks", columnDefinition = "VARCHAR(100)")
private Set<DayOfWeek> ableDayOfWeeks;

@Comment("event_participable_time에서 모두가 합의한 시간")
@Column(name = "start_time")
private LocalTime startTime;

@Comment("event_participable_time에서 모두가 합의한 시간")
@Column(name = "end_time")
private LocalTime endTime;

@Comment("종속된 event")
@ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "event_id", updatable = false)
private Event event;

protected EventAbleTime() {
}

@Builder
public EventAbleTime(Set<DayOfWeek> ableDayOfWeeks, LocalTime startTime, LocalTime endTime, Event event) {
    this.ableDayOfWeeks = ableDayOfWeeks;
    this.startTime      = startTime;
    this.endTime        = endTime;
    this.event          = event;
}

public EventAbleTime update(Set<DayOfWeek> ableDayOfWeeks, LocalTime startTime, LocalTime endTime) {
    this.ableDayOfWeeks = ableDayOfWeeks;
    this.startTime      = startTime;
    this.endTime        = endTime;
    return this;
}
}