package com.mimi.w2m.backend.converter.db;

import com.mimi.w2m.backend.domain.type.TimeRange;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.*;

@Converter
public class SetTimeRangeConverter implements AttributeConverter<Set<TimeRange>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<TimeRange> attribute) {
        if (Objects.isNull(attribute) || attribute.isEmpty()) {
            return "";
        }

        final var builder = new StringBuilder(20 * attribute.size());
        for (var time : attribute) {
            builder.append(time.toString())
                    .append(DELIMITER);
        }

        return builder.toString();
    }

    @Override
    public Set<TimeRange> convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData) || dbData.isEmpty()) {
            return Set.of();
        }

        final String[] split = dbData.split(DELIMITER);
        final var times = new HashSet<TimeRange>(split.length);
        for (var time : split) {
            times.add(TimeRange.of(time));
        }
        return times;
    }

}