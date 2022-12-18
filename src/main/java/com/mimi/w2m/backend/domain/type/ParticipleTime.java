package com.mimi.w2m.backend.domain.type;

import com.mimi.w2m.backend.config.exception.InvalidValueException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * ParticipleTime : 참여가능한 start & end time
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/19
 **/
@Data
@Builder
@Schema(type = "String", title = "이벤트의 시간 정보",
        description = "날짜는 대문자로! day=[MONDAY,...], 시간 범위는 hh:mm:ss-hh:mm:ss 형식으로하며 각 시간 범위의 구분은 |로 한다",
        example = "MONDAY[T]10:00:00-12:00:00|13:00:00-14:00:00|")
public class ParticipleTime {
    @Schema(type = "String", description = "월요일(MONDAY) - 일요일(SUNDAY)", example = "MONDAY",
            allowableValues = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", " SATURDAY", "SUNDAY"})
    @NotNull
    private final DayOfWeek week;
    @Schema(type = "Array", description = "참여가능한 시작시간-종료시간의 리스트", example = "[10:00:00-12:00:00,13:00:00-14:00:00]")
    @NotNull
    private final Set<TimeRange> ranges;

    public static ParticipleTime of(String participleTimeStr) {
        final var parsedStrs = participleTimeStr.split("\\[T]");
        if (parsedStrs.length != 2) {
            throw new InvalidValueException("Invalid ParticipleTime : " + participleTimeStr, "유효하지 않은 참여 가능한 시간 형식");
        }

        final var day = DayOfWeek.valueOf(parsedStrs[0]);
        final var ranges = new HashSet<TimeRange>();
        for (String s : parsedStrs[1].split("\\|")) {
            TimeRange of = TimeRange.of(s);
            ranges.add(of);
        }

        return new ParticipleTime(day, ranges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(week, ranges);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (Objects.isNull(o) || !(o instanceof ParticipleTime)) {
            return false;
        }
        return Objects.equals(this.hashCode(), o.hashCode());
    }

    /**
     * @author teddy
     * @since 2022/11/19
     **/
    @Override
    public String toString() {
        final var builder = new StringBuilder();
        final var str = builder.append(week.name())
                .append("[T]");
        ranges.forEach(range -> builder.append(range.toString())
                .append("|"));
        return str.toString();
    }
}
