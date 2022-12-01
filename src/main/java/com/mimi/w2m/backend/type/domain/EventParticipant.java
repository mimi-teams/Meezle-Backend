package com.mimi.w2m.backend.type.domain;

import com.mimi.w2m.backend.type.common.ParticipleTime;
import com.mimi.w2m.backend.type.converter.db.SetParticipleTimeConverter;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.Objects;
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
@Table(name = "event_participant", uniqueConstraints = {@UniqueConstraint(columnNames = {"event_id", "user_id"}),
                                                        @UniqueConstraint(columnNames = {"event_id", "guest_id"})})
public class EventParticipant {
    @Comment("참여 가능한 시간")
    @Convert(converter = SetParticipleTimeConverter.class)
    @Column(name = "able_days_and_times")
    Set<ParticipleTime> ableDaysAndTimes;
    @Id
    @Column(name = "event_participant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    @Comment("연관된 event")
    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", updatable = false, nullable = false)
    private Event event;

    @Comment("참여자의 정보(user)")
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Comment("참여자의 정보(user)")
    @ManyToOne(targetEntity = Guest.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private Guest guest;

    protected EventParticipant() {
    }

    @Builder
    public EventParticipant(Set<ParticipleTime> ableDaysAndTimes, Event event, User user, Guest guest) {
        this.ableDaysAndTimes = ableDaysAndTimes;
        this.event            = event;
        this.user             = user;
        this.guest            = guest;
    }

    public EventParticipant update(Set<ParticipleTime> ableDaysAndTimes) {
        this.ableDaysAndTimes = ableDaysAndTimes;
        return this;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {return false;}
        EventParticipant that = (EventParticipant) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + "id = " + id + ", " + "ableDaysAndTimes = " + ableDaysAndTimes + ")";
    }
}
