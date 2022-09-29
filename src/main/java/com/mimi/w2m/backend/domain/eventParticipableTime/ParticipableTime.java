package com.mimi.w2m.backend.domain.eventParticipableTime;

import com.mimi.w2m.backend.domain.event.Event;
import com.mimi.w2m.backend.domain.eventParticipant.Participant;
import com.mimi.w2m.backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author : teddy
 * @since : 2022/09/30
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "w2m_event_participable_time")
public class ParticipableTime {
    @Id
    @Column(name = "participable_time_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "able_date", nullable = false)
    @Comment(value = "입력 날짜")
    LocalDateTime ableTime;

    @Column(name = "start_time")
    @Comment(value = "날짜별 가능한 시작 시간")
    LocalDateTime startTime;

    @Column(name = "end_time")
    @Comment(value = "날짜별 가능한 종료 시간")
    LocalDateTime endTime;

    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", unique = true, updatable = false)
    Event event;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, updatable = true)
    Optional<User> user;

    @ManyToOne(targetEntity = Participant.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", unique = true, updatable = true)
    Optional<Participant> participant;

    @Builder
    public ParticipableTime(LocalDateTime ableTime, LocalDateTime startTime, LocalDateTime endTime, Event event, User user, Participant participant) {
        this.ableTime = ableTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.event = event;
        this.user = Optional.ofNullable(user);
        this.participant = Optional.ofNullable(participant);
    }

    public ParticipableTime updateTime(LocalDateTime ableTime, LocalDateTime startTime, LocalDateTime endTime) {
        this.ableTime = ableTime;
        this.startTime = startTime;
        this.endTime = endTime;
        return this;
    }

    public ParticipableTime updateUser(User user) {
        this.user = Optional.ofNullable(user);
        return this;
    }

    public ParticipableTime updateParticipant(Participant participant) {
        this.participant = Optional.ofNullable(participant);
        return this;
    }
}
