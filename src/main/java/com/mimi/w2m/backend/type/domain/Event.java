package com.mimi.w2m.backend.type.domain;

import com.mimi.w2m.backend.type.common.ParticipleTime;
import com.mimi.w2m.backend.type.converter.db.ListParticipleTimeConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Event
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/16
 **/

@Entity
@Getter
@Table(name = "event")
public class Event extends BaseTimeEntity {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("이벤트 제목. 최대 길이는 200")
    @Column(name = "title", length = 200, nullable = false, columnDefinition = "VARCHAR(200)")
    private String title;

    @Comment("삭제 일자, 삭제가 안되었으면 null")
    @Setter
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Comment("만료 일자, 만료 일자가 없으면 null")
    @Column(name = "d_day")
    private LocalDateTime dDay;

    @Comment("선택 가능한 시간(Default : 모든 요일 & 모든 시간)")
    @Convert(converter = ListParticipleTimeConverter.class)
    @Column(name = "selectable_days_and_times", nullable = false)
    private List<ParticipleTime> selectableDaysAndTimes;

    @Comment("선택된 시간(Default : null)")
    @Setter
    @Convert(converter = ListParticipleTimeConverter.class)
    @Column(name = "selected_days_and_times")
    private List<ParticipleTime> selectedDaysAndTimes;

    @Comment("이벤트의 대표 색상. Backend에서 설정해 Front에 전달한다(브라우저마다 동일하게 보이게 만들려고!)")
    @Column(name = "color", nullable = false)
    private Color color;

    @Comment("이벤트 세부 설명(Default : EMPTY STRING)")
    @Column(name = "description", nullable = false, length = 1000, columnDefinition = "VARCHAR(1000)")
    private String description;

    @Comment("이벤트를 생성한 사용자")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "host_id", updatable = false)
    private User host;

    protected Event() {
    }

    @Builder
    public Event(String title, LocalDateTime dDay, List<ParticipleTime> selectableDaysAndTimes, Color color,
                 String description, User host) {
        this.title                  = title;
        this.dDay                   = dDay;
        this.selectableDaysAndTimes = selectableDaysAndTimes;
        this.color                  = color;
        this.description            = Objects.nonNull(description) ? description : Strings.EMPTY;
        this.host                   = host;
    }

    public Event update(String title, LocalDateTime dDay, List<ParticipleTime> selectableDaysAndTimes, Color color,
                        String description) {
        this.title                  = title;
        this.dDay                   = dDay;
        this.selectableDaysAndTimes = selectableDaysAndTimes;
        this.color                  = color;
        this.description            = Objects.nonNull(description) ? description : Strings.EMPTY;
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
        Event event = (Event) o;
        return id != null && Objects.equals(id, event.id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + "id = " + getId() + ", " + "createdDate = " + getCreatedDate() +
               ", " + "lastModifiedDate = " + getLastModifiedDate() + ", " + "title = " + getTitle() + ", " +
               "deletedAt = " + getDeletedAt() + ", " + "dDay = " + dDay + ", " + "selectableDaysAndTimes = " +
               getSelectableDaysAndTimes() + ", " + "selectedDaysAndTimes = " + getSelectedDaysAndTimes() + ", " +
               "color = " + getColor() + ", " + "description = " + getDescription() + ")";
    }
}