package com.mimi.w2m.backend.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * `Set<DayOfWeek>` <--> "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY,"
 *
 * @author yeh35
 * @since 2022-11-05
 */
@Converter
public class SetDayOfWeekConverter implements AttributeConverter<Set<DayOfWeek>, String> {

@Override
public String convertToDatabaseColumn(Set<DayOfWeek> attribute) {
    if(Objects.isNull(attribute)) {
        return null;
    } else {
        final var builder = new StringBuilder();
        attribute.forEach(dayOfWeek -> {
            builder.append(dayOfWeek.name());
            builder.append(",");
        });
        return builder.toString();
    }
}

@Override
public Set<DayOfWeek> convertToEntityAttribute(String dbData) {
    if(Objects.isNull(dbData)) {
        return new HashSet<>();
    } else {
        final var split = dbData.split(",");
        final var set   = new HashSet<DayOfWeek>();
        for(String item : split) {
            set.add(DayOfWeek.valueOf(item));
        }
        return set;
    }
}
}