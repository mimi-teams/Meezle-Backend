package com.mimi.w2m.backend.dto.event;

import com.mimi.w2m.backend.domain.type.ParticipleTime;
import com.mimi.w2m.backend.domain.type.TimeRange;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "이벤트에서 선택 가능한 시간")
public class SelectableParticipleTimeDto {

    @Schema(example = "[\"MONDAY\", \"SUNDAY\"]")
    public Set<DayOfWeek> selectedDayOfWeeks;

    @Schema(example = "05:05:00")
    public LocalTime beginTime;

    @Schema(example = "15:05:00")
    public LocalTime endTime;

    
    public Set<ParticipleTime> toParticipleTimeSet() {

        final var resultSet = new HashSet<ParticipleTime>(selectedDayOfWeeks.size());

        for (final DayOfWeek dayOfWeek : selectedDayOfWeeks) {
            final var timeRange = new TimeRange(beginTime, endTime);
            final var participleTime = ParticipleTime.builder()
                    .week(dayOfWeek)
                    .ranges(Set.of(timeRange))
                    .build();

            resultSet.add(participleTime);
        }

        return resultSet;
    }

}
