package com.mimi.w2m.backend.type.common;

import org.springframework.data.util.Pair;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record TimeRange(LocalTime beginTime,
                        LocalTime endTime) implements Comparable<TimeRange> {

    public static TimeRange of(String timeRangeStr) throws IOException {
        final var parsedStrs = timeRangeStr.split(delimiter());
        if(parsedStrs.length != 2) {
            throw new IOException();
        }
        final var begTime = LocalTime.from(formatter().parse(parsedStrs[0]));
        final var endTime = LocalTime.from((formatter().parse(parsedStrs[1])));
        return fixOrder(new TimeRange(begTime, endTime));
    }

    private static String delimiter() {
        return "-";
    }

    private static DateTimeFormatter formatter() {
        return DateTimeFormatter.ISO_LOCAL_TIME;
    }

    public static TimeRange fixOrder(TimeRange range) {
        return range.beginTime.isBefore(range.endTime) ? range : new TimeRange(range.endTime, range.beginTime);
    }

    @Override
    public String toString() {
        return formatter().format(beginTime) + delimiter() + formatter().format(endTime);
    }

    /**
     * beginTime 을 기준으로 정렬한다
     *
     * @author teddy
     * @since 2022/12/01
     **/
    @Override
    public int compareTo(TimeRange o) {
        return beginTime.compareTo(o.beginTime);
    }

    public static class Operator {
        /**
         * 시간 계산을 위한 연산. Disjoint Set 인 경우, Pair 의 a, b가 모두 채워지고, 그렇지 않다면 a 만 채워진다(b=EMPTY)
         *
         * @author teddy
         * @since 2022/12/01
         **/
        public static TimeRange EMPTY = new TimeRange(LocalTime.MAX, LocalTime.MAX);
        public static TimeRange FULL  = new TimeRange(LocalTime.MIN, LocalTime.MAX);

        public static Pair<TimeRange, TimeRange> intersection(TimeRange a, TimeRange b) {
            final var orderedA = fixOrder(a);
            final var orderedB = fixOrder(b);

            final var beginTime = orderedA.beginTime.isBefore(orderedB.beginTime) ? orderedB.beginTime
                                                                                  : orderedA.beginTime;
            final var endTime = orderedA.endTime.isAfter(orderedB.endTime) ? orderedB.endTime : orderedA.endTime;
            if(beginTime.isAfter(endTime)) {
                return Pair.of(EMPTY, EMPTY);
            } else {
                return Pair.of(new TimeRange(beginTime, endTime), EMPTY);
            }
        }

        public static Pair<TimeRange, TimeRange> union(TimeRange a, TimeRange b) {
            final var orderedA = fixOrder(a);
            final var orderedB = fixOrder(b);
            if(orderedA.endTime.isBefore(orderedB.beginTime) || orderedB.endTime.isBefore(orderedA.beginTime)) {
                return Pair.of(orderedA, orderedB);
            } else {
                final var beginTime = orderedA.beginTime.isBefore(orderedB.beginTime) ? orderedA.beginTime
                                                                                      : orderedB.beginTime;
                final var endTime = orderedA.endTime.isAfter(orderedB.endTime) ? orderedA.endTime : orderedB.endTime;
                return Pair.of(new TimeRange(beginTime, endTime), EMPTY);
            }
        }

        public static Pair<TimeRange, TimeRange> not(TimeRange a) {
            final var orderedA = fixOrder(a);
            return Pair.of(new TimeRange(LocalTime.MIN, orderedA.beginTime),
                           new TimeRange(orderedA.endTime, LocalTime.MAX));
        }
    }
}