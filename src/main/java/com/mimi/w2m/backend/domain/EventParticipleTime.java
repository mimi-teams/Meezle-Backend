package com.mimi.w2m.backend.domain;

import com.mimi.w2m.backend.domain.converter.SetDayOfWeekConverter;
import com.mimi.w2m.backend.domain.converter.SetParticipleTimeConverter;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.DayOfWeek;
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
@Table(name = "meezle_event_participle_time")
public class EventParticipleTime {
@Id
@Column(name = "event_participle_time_id")
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Comment(value = "각 참여자가 입력한 자신이 가능한 요일")
@Convert(converter = SetDayOfWeekConverter.class)
@Column(name = "able_day_of_weeks", nullable = false, columnDefinition = "VARCHAR(100)")
private Set<DayOfWeek> ableDayOfWeeks;

@Comment(value = "각 참여자가 입력한 날짜별 가능한 시간의 집합")
@Convert(converter = SetParticipleTimeConverter.class)
@Column(name = "able_times", nullable = false, columnDefinition = "VARCHAR(500)")
private Set<ParticipleTime> participleTimes;

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

protected EventParticipleTime() {
}

@Builder
public EventParticipleTime(Set<DayOfWeek> ableDayOfWeeks, Set<ParticipleTime> participleTimes, Event event,
                           User user, Participant participant) {
    this.ableDayOfWeeks  = ableDayOfWeeks;
    this.participleTimes = participleTimes;
    this.event           = event;
    this.user            = user;
    this.participant     = participant;
}

public EventParticipleTime update(Set<DayOfWeek> ableDayOfWeeks, Set<ParticipleTime> participleTimes) {
    this.ableDayOfWeeks  = ableDayOfWeeks;
    this.participleTimes = participleTimes;
    return this;
}
}
