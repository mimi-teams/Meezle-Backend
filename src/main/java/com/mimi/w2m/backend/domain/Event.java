package com.mimi.w2m.backend.domain;

import com.mimi.w2m.backend.domain.converter.SetDayOfWeekConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

/**
 * @author : teddy
 * @since : 2022/09/29
 */

@Entity
@Getter
@Setter //TODO 빼줘
@Table(name = "mimi_event")
public class Event extends BaseTimeEntity {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("이벤트 제목")
    @Column(name="title", length = 200, nullable = false)
    private String title;

    @Comment("삭제 일자, 삭제가 안되었으면 null")
    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @Comment("만료 일자, 만료 일자가 없으면 null")
    @Column(name = "d_day")
    private LocalDateTime dDay;

    @Comment("만료 일자, 만료 일자가 없으면 null")
    @Convert(converter = SetDayOfWeekConverter.class)
    @Column(name = "dayOfWeeks", nullable = false, columnDefinition = "VARCHAR(100)")
    private Set<DayOfWeek> dayOfWeeks;

    @Comment("이벤트 입력 시작 시간")
    @Column(name = "begin_time", nullable = false)
    private LocalTime beginTime;

    @Comment("이벤트 입력 종료 시간")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Comment("이벤트 입력 종료 시간")
    @Column(name = "end_time", nullable = false)
    private Color color;

    @Comment("이벤트 입력 종료 시간")
    @Column(name = "description", nullable = false, columnDefinition = "VARCHAR(1000)")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;


    @Builder
    public Event(String title, LocalDateTime dDay, Set<DayOfWeek> dayOfWeeks, LocalTime beginTime, LocalTime endTime, User user, Color color, String description) {
        this.title = title;
        this.dDay = dDay;
        this.dayOfWeeks = dayOfWeeks;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.user = user;
        this.deletedDate = null;
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