package com.mimi.w2m.backend.domain.event;

import com.mimi.w2m.backend.domain.core.BaseTimeEntity;
import com.mimi.w2m.backend.domain.eventAbleTime.AbleTime;
import com.mimi.w2m.backend.domain.eventParticipableTime.ParticipableTime;
import com.mimi.w2m.backend.domain.eventParticipant.Participant;
import com.mimi.w2m.backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : teddy
 * @since : 2022/09/29
 */

@Entity
@Getter
@NoArgsConstructor
@Table(name = "event")
public class Event extends BaseTimeEntity {

    @Id
    @Column(name = "event_id")
    @GeneratedValue
    private Long id;

    @Column(name="name", length = 200, nullable = false)
    private String name;

    @Column(name = "deleted_date", nullable = false)
    private LocalDateTime deletedDate;

    @Column(name = "d_day", nullable = false)
    private LocalDateTime dDay;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", unique = true, updatable = false)
    private User user;

    @OneToMany(targetEntity = AbleTime.class, mappedBy = "event", fetch = FetchType.LAZY)
    private List<AbleTime> ableTimeList;

    @OneToMany(targetEntity = Participant.class, mappedBy = "event", fetch = FetchType.LAZY)
    private List<Participant> participantList;

    @OneToMany(targetEntity = ParticipableTime.class, mappedBy = "event", fetch = FetchType.LAZY)
    private List<ParticipableTime> participableTimeList;

    @Builder
    public Event(String name, LocalDateTime deletedDate, LocalDateTime dDay, User user) {
        this.name = name;
        this.deletedDate = deletedDate;
        this.dDay = dDay;
        this.user = user;
    }

    public Event update(String name, LocalDateTime deletedDate, LocalDateTime dDay) {
        this.name = name;
        this.deletedDate = deletedDate;
        this.dDay = dDay;
        return this;
    }
}