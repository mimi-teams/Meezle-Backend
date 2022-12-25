package com.mimi.w2m.backend.domain;

import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * EventParticipableTime
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/16
 **/
@Entity
@Getter
@Table(
        name = "event_participant",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"event_id", "user_id"}),
                @UniqueConstraint(columnNames = {"event_id", "guest_id"})
        }
)
public class EventParticipant {

    @Id
    @GenericGenerator(name="sequential_uuid", strategy="com.mimi.w2m.backend.domain.generator.SequentialUUIDGenerator")
    @GeneratedValue(generator="sequential_uuid")
    @Column(name = "event_participant_id", columnDefinition = "BINARY(16)")
    private UUID id;

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


    protected EventParticipant(Event event, User user, Guest guest) {
        this.event = event;
        this.user = user;
        this.guest = guest;
    }

    /**
     * 유저가 이벤트 참여하는 경우
     */
    public static EventParticipant ofUser(Event event, User user) {
        return new EventParticipant(event, user, null);
    }

    /**
     * 게스트가 이벤트 참여하는 경우
     */
    public static EventParticipant ofGuest(Event event, Guest guest) {
        return new EventParticipant(event, null, guest);
    }
}
