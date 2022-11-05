package com.mimi.w2m.backend.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author : teddy
 * @since : 2022/09/29
 */

@Entity
@Getter
@Setter
@Table(name = "mimi_event")
public class Event extends BaseTimeEntity {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("")
    @Column(name="title", length = 200, nullable = false)
    private String title;

    @Comment("")
    @Column(name = "deleted_date")
    private LocalDateTime deletedDate = null;

    @Column(name = "d_day", nullable = false)
    private LocalDateTime dDay;
    /**
     * @JoinColumn(unique = boolean)은 현재 Entity에서 해당 FK가 중복 가능한지 표시한다.
     */
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Builder
    public Event(String title, LocalDateTime deletedDate, LocalDateTime dDay, User user) {
        this.title = title;
        this.deletedDate = deletedDate;
        this.dDay = dDay;
        this.user = user;
    }

    protected Event() {
    }

    public Event update(String title, LocalDateTime deletedDate, LocalDateTime dDay) {
        this.title = title;
        this.deletedDate = deletedDate;
        this.dDay = dDay;
        return this;
    }
}