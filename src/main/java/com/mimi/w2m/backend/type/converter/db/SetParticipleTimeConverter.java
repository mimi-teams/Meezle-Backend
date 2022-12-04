package com.mimi.w2m.backend.type.converter.db;

import com.mimi.w2m.backend.type.common.ParticipleTime;
import com.mimi.w2m.backend.type.common.TimeRange;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.DayOfWeek;
import java.util.*;

/**
 * SetParticipleTimeConverter(Array delimiter : ",")
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/19
 **/
@Converter
public class SetParticipleTimeConverter implements AttributeConverter<Set<ParticipleTime>, String> {

    @Override
    public String convertToDatabaseColumn(Set<ParticipleTime> attribute) {
        if (Objects.isNull(attribute) || attribute.isEmpty()) {
            return null;
        } else {
            final var builder = new StringBuilder();
            for (var time : attribute) {
                builder.append(time.toString())
                        .append(delimiter());
            }
            return builder.toString();
        }
    }

    @Override
    public Set<ParticipleTime> convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData) || dbData.isEmpty()) {
            return Set.of();
        } else {
            final var times = new HashSet<ParticipleTime>();
            for (var time : dbData.split(delimiter())) {
                times.add(ParticipleTime.of(time));
            }
            return times;
        }
    }

    private static String delimiter() {
        return ",";
    }

    public Map<DayOfWeek, Set<TimeRange>> convertToMap(Set<ParticipleTime> participleTimes) {
        if (Objects.isNull(participleTimes) || participleTimes.isEmpty()) {
            return Map.of();
        } else {
            final var map = new HashMap<DayOfWeek, Set<TimeRange>>();
            participleTimes.forEach(p -> map.put(p.getDay(), p.getRanges()));
            return map;
        }
    }

    public Set<ParticipleTime> convertToSet(Map<DayOfWeek, Set<TimeRange>> participleTimes) {
        if (Objects.isNull(participleTimes) || participleTimes.isEmpty()) {
            return Set.of();
        } else {
            final var set = new HashSet<ParticipleTime>();
            participleTimes.forEach((d, t) -> set.add(ParticipleTime.builder()
                    .day(d)
                    .ranges(t)
                    .build()));
            return set;
        }
    }
}