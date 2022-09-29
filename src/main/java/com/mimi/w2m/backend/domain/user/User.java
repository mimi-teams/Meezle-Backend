package com.mimi.w2m.backend.domain.user;

import com.mimi.w2m.backend.domain.core.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;


@Entity
@Getter
@Table(name = "w2m_user")
public class User extends BaseTimeEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 200, nullable = false)
    @Comment("이름")
    private String name;

    @Column(name = "email", length = 200, nullable = false)
    @Comment("이메일")
    private String email;

//    @OneToMany(targetEntity = Event.class, mappedBy = "user", fetch = FetchType.LAZY)
//    private List<Event> eventList;
//
//    @OneToMany(targetEntity = ParticipableTime.class, mappedBy = "user", fetch = FetchType.LAZY)
//    private List<ParticipableTime> participableTimeList;

    @Builder
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    protected User() {
    }

    public User update(String name, String email) {
        this.name = name;
        this.email = email;
        return this;
    }
}
