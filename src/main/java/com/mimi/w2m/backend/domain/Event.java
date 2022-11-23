package com.mimi.w2m.backend.domain;

import com.mimi.w2m.backend.domain.converter.ParticipleTimeConverter;
import com.mimi.w2m.backend.domain.converter.SetDayOfWeekConverter;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
import lombok.Builder;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * Event
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/16
 **/

@Entity
@Getter
@Table(name = "meezle_event")
public class Event extends BaseTimeEntity {

@Id
@Column(name = "event_id")
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Comment("이벤트 제목")
@Column(name = "title", length = 200, nullable = false)
private String title;

@Comment("삭제 일자, 삭제가 안되었으면 null")
@Column(name = "deleted_date")
private LocalDateTime deletedDate;

@Comment("만료 일자, 만료 일자가 없으면 null")
@Column(name = "d_day")
private LocalDateTime dDay;

@Comment("선택된 요일(event_able_time에서 결정된 요일을 명시한다). 확정되지 않았으면 null")
@Convert(converter = SetDayOfWeekConverter.class)
@Column(name = "day_of_weeks", columnDefinition = "VARCHAR(100)")
private Set<DayOfWeek> dayOfWeeks;

@Comment("각 요일의 이벤트 시작 시간 및 종료 시간. 확정되지 않았으면 null")
@Convert(converter = ParticipleTimeConverter.class)
@Column(name = "participle_time", columnDefinition = "VARCHAR(500)")
private ParticipleTime participleTime;

@Comment("이벤트 표시 색상. Backend에서 설정해 Front에 전달한다(브라우저마다 동일하게 보이게 만들려고!)")
@Column(name = "color", nullable = false)
private Color color;

@Comment("이벤트 세부 설명. 없으면 \"\"")
@Column(name = "description", nullable = false, columnDefinition = "VARCHAR(1000)")
private String description;

@Comment("이벤트를 생성한 사용자")
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "host_id", updatable = false)
private User user;

@Builder
public Event(String title, LocalDateTime dDay, Set<DayOfWeek> dayOfWeeks, ParticipleTime participleTime,
             User user, Color color, String description) {
    this.title          = title;
    this.dDay           = dDay;
    this.dayOfWeeks     = dayOfWeeks;
    this.participleTime = participleTime;
    this.user           = user;
    this.deletedDate    = null;
    this.color          = color;
    this.description    = Objects.isNull(description) ? Strings.EMPTY : description;
}

protected Event() {
}

public Event update(String title, String description, Color color, LocalDateTime dDay) {
    this.title       = title;
    this.dDay        = dDay;
    this.color       = color;
    this.description = description;
    return this;
}

public Event update(Set<DayOfWeek> dayOfWeeks, ParticipleTime participleTime) {
    this.dayOfWeeks     = dayOfWeeks;
    this.participleTime = participleTime;
    return this;
}

public Event delete() {
    this.deletedDate = LocalDateTime.now();
    return this;
}
}