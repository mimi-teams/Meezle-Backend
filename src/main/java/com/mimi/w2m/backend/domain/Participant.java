package com.mimi.w2m.backend.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

/**
 * @author : teddy
 * @since : 2022/09/30
 */
@Getter
@Entity
@Table(name = "mimi_participant")
public class Participant extends BaseTimeEntity {
    @Id
    @Column(name = "participant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "password", length = 200)
    private String password;

    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", updatable = false)
    private Event event;

    @Builder
    public Participant(String name, String password, Event event) {
        this.name = name;
        this.password = password;
        this.event = event;
    }

    protected Participant() {
    }

    public Participant update(String name, String password) {
        this.name = name;
        this.password = password;
        return this;
    }
}