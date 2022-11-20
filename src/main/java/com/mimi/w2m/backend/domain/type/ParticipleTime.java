package com.mimi.w2m.backend.domain.type;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * ParticipleTime : 참여가능한 start & end time
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/19
 **/
public record ParticipleTime(@JsonProperty(required = true, value = "beginTime") LocalTime beginTime,
                             @JsonProperty(required = true, value = "endTime") LocalTime endTime) {
    public static ParticipleTime of(String participleTimeStr) {
        var splitTimes = participleTimeStr.split("-");
        var formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        var startTime = LocalTime.parse(splitTimes[0], formatter);
        var endTime = LocalTime.parse(splitTimes[1], formatter);
        return new ParticipleTime(startTime, endTime);
    }

    /**
     * 시간은 HH:mm:ss 형식;
     * Instance는 beginTime-endTime 형식
     * @author teddy
     * @since 2022/11/19
     *
    **/
    @Override
    public String toString() {
        var formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return beginTime.format(formatter) + "-" + endTime.format(formatter);
    }
}