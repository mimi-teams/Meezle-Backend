package com.mimi.w2m.backend.domain.eventParticipant;

import com.mimi.w2m.backend.domain.core.BaseTimeEntity;
import com.mimi.w2m.backend.domain.event.Event;
import com.mimi.w2m.backend.domain.eventParticipableTime.ParticipableTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * @author : teddy
 * @since : 2022/09/30
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "w2m_participant")
public class Participant extends BaseTimeEntity {
    @Id
    @Column(name = "participant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", length = 200, nullable = false)
    String name;

    @Column(name = "password", length = 200)
    String password;

    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", updatable = false, unique = true)
    private Event event;

    @OneToMany(targetEntity = ParticipableTime.class, mappedBy = "participant", fetch = FetchType.LAZY)
    private List<ParticipableTime> participableTimeList;

    @Builder
    public Participant(String name, String password, Event event) {
        this.name = name;
        this.password = password;
        this.event = event;
    }
}