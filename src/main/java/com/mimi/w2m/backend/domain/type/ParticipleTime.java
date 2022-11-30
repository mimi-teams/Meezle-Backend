package com.mimi.w2m.backend.domain.type;

import com.mimi.w2m.backend.error.InvalidValueException;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;

/**
 * ParticipleTime : 참여가능한 start & end time
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/19
 **/
@Schema(description = "참여 가능한 시간", requiredProperties = {"beginTime", "endTime"})
public final class ParticipleTime {
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
//    @Schema(type = "String", description = "시작 시간", pattern = "^\\d" + "{2}:\\d{2}:\\d{2}\\.\\d{3}$")
    private final LocalTime beginTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
//    @Schema(type = "String", description = "종료 시간", pattern = "^\\d" + "{2}:\\d{2}:\\d{2}\\.\\d{3}$")
    private final LocalTime endTime;
    Long id;

    private ParticipleTime(LocalTime beginTime, LocalTime endTime) {
        this.beginTime = beginTime;
        this.endTime   = endTime;
    }

    public static ParticipleTime of(String participleTimeStr) throws InvalidValueException {
        var splitTimes = participleTimeStr.split("-");
        var formatter  = DateTimeFormatter.ofPattern("HH:mm:ss");
        try {
            var beginTime = LocalTime.parse(splitTimes[0], formatter);
            var endTime   = LocalTime.parse(splitTimes[1], formatter);
            if(verify(beginTime, endTime)) {
                return new ParticipleTime(beginTime, endTime);
            } else {
                throw new InvalidValueException("유효하지 않은 시간 형식 : " + participleTimeStr, "유효하지 않은 시간 형식");
            }
        } catch(DateTimeParseException e) {
            throw new InvalidValueException("유효하지 않은 시간 형식 : " + participleTimeStr, "유효하지 않은 시간 형식");
        }
    }

    private static Boolean verify(LocalTime beginTime, LocalTime endTime) {
        return beginTime.isBefore(endTime);
    }

    public static ParticipleTime of(Map<String, LocalTime> participleTimeMap) throws InvalidValueException {
        var beginTime = participleTimeMap.get("beginTime");
        var endTime   = participleTimeMap.get("endTime");

        if(Objects.isNull(beginTime) || Objects.isNull(endTime)) {
            throw new InvalidValueException("유효하지 않은 시간 형식 : " + participleTimeMap, "유효하지 않은 시간 형식");
        } else {
            return new ParticipleTime(beginTime, endTime);
        }
    }

    public LocalTime beginTime() {
        return beginTime;
    }

    public LocalTime endTime() {
        return endTime;
    }

    /**
     * 시간은 HH:mm:ss 형식; Instance는 beginTime-endTime 형식
     *
     * @author teddy
     * @since 2022/11/19
     **/
    @Override
    public String toString() {
        final var formatter = DateTimeFormatter.ISO_LOCAL_TIME;
        return beginTime.format(formatter) + "-" + endTime.format(formatter);
    }
}