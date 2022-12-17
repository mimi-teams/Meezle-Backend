package com.mimi.w2m.backend.domain;

import com.mimi.w2m.backend.converter.db.SetTimeRangeConverter;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
import com.mimi.w2m.backend.domain.type.TimeRange;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

/**
 * 이벤트 참여 가능한 시간
 * ParticipleTime가 해당 엔티티의 추상형태이다.
 *
 * @author yeh35
 * @since 2022-12-17
 */
@Entity
@Getter
@Table(name = "event_selectable_participle_time")
public class EventSelectableParticipleTime extends BaseTimeEntity {

    @Id
    @Column(name = "event_selectable_participle_time_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("삭제 일자, 삭제가 안되었으면 null")
    @Column(name = "week")
    private DayOfWeek week;

    @Comment("삭제 일자, 삭제가 안되었으면 null")
    @Convert(converter = SetTimeRangeConverter.class)
    @Column(name = "time_ranges", nullable = false)
    private Set<TimeRange> timeRanges;

    @Comment("연관된 event")
    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", updatable = false, nullable = false)
    private Event event;


    @SuppressWarnings("unused")
    protected EventSelectableParticipleTime() {
    }

    @SuppressWarnings("unused")
    @Builder
    public EventSelectableParticipleTime(DayOfWeek week, Set<TimeRange> timeRanges, Event event) {
        this.week = week;
        this.timeRanges = timeRanges;
        this.event = event;
    }

    public ParticipleTime toParticipleTime() {
        return ParticipleTime.builder()
                .week(week)
                .ranges(timeRanges)
                .build();
    }

    public static Set<EventSelectableParticipleTime> of(Event event, Set<ParticipleTime> participleTimeSet) {
        final var resultSet = new HashSet<EventSelectableParticipleTime>(participleTimeSet.size());

        for (final var participleTime : participleTimeSet) {
            resultSet.add(EventSelectableParticipleTime.builder()
                    .event(event)
                    .week(participleTime.getWeek())
                    .timeRanges(participleTime.getRanges())
                    .build());
        }

        return resultSet;
    }
}