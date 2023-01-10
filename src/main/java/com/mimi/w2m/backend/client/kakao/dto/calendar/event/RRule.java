package com.mimi.w2m.backend.client.kakao.dto.calendar.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.DayOfWeek;
import java.util.List;

/**
 * RRule :  <a href="https://datatracker.ietf.org/doc/html/rfc5545">RFC5545</a> 의 RRULE 형식
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/10
 **/
public record RRule(
        @JsonProperty(value = "FREQ")
        FreqType freq,
        @JsonProperty(value = "BYDAY")
        List<DayOfWeek> byDay
//        @DateTimeFormat(pattern = "yyyyMMdd`T`HHmmss`Z`")
//        @JsonProperty(value = "UNTIL")
//        LocalDateTime until
) {

    public enum FreqType {
        WEEKLY("WEEKLY"),
        ;
        @Getter
        private final String value;

        FreqType(String value) {
            this.value = value;
        }
    }
}