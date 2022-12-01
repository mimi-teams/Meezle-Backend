package com.mimi.w2m.backend.type.common;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record TimeRange(LocalTime beginTime,
                        LocalTime endTime) {
    public static TimeRange of(String timeRangeStr) throws IOException {
        final var parsedStrs = timeRangeStr.split(delimiter());
        if(parsedStrs.length != 2) {
            throw new IOException();
        }
        final var begTime = LocalTime.from(formatter().parse(parsedStrs[0]));
        final var endTime = LocalTime.from((formatter().parse(parsedStrs[1])));
        if(verify(begTime, endTime)) {
            throw new IOException();
        }
        return new TimeRange(begTime, endTime);
    }
    private static String delimiter() {
        return "-";
    }

    private static DateTimeFormatter formatter() {
        return DateTimeFormatter.ISO_LOCAL_TIME;
    }

    private static Boolean verify(LocalTime beginTime, LocalTime endTime) {
        return beginTime.isBefore(endTime);
    }

    @Override
    public String toString() {
        return formatter().format(beginTime) + delimiter() + formatter().format(endTime);
    }
}