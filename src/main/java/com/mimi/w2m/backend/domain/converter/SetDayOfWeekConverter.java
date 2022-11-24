package com.mimi.w2m.backend.domain.converter;

import com.mimi.w2m.backend.error.InvalidValueException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
public Set<DayOfWeek> convertToEntityAttribute(String dbData) throws InvalidValueException {
    if(Objects.isNull(dbData)) {
        return new HashSet<>();
    } else {
        final var split = dbData.split(",");
        final var validRange = Arrays
                                       .stream(DayOfWeek.values())
                                       .map(Enum::name)
                                       .collect(Collectors.toSet());
        final var set = new HashSet<DayOfWeek>();
        for(String item : split) {
            if(validRange.contains(item)) {
                set.add(DayOfWeek.valueOf(item));
            } else {
                throw new InvalidValueException("유효하지 않은 요일 : " + item, "유효하지 않은 요일");
            }
        }
        return set;
    }
}
}