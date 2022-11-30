package com.mimi.w2m.backend.type.converter.db;

import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.type.common.ParticipleTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ListParticipleTimeConverter(Array delimiter : ",")
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/19
 **/
@Converter
public class ListParticipleTimeConverter implements AttributeConverter<List<ParticipleTime>, String> {

    @Override
    public String convertToDatabaseColumn(List<ParticipleTime> attribute) {
        if(Objects.isNull(attribute) || attribute.isEmpty()) {
            return null;
        } else {
            final var builder = new StringBuilder();
            for(var time : attribute) {
                builder.append(time.toString())
                       .append(delimiter());
            }
            return builder.toString();
        }
    }

    @Override
    public List<ParticipleTime> convertToEntityAttribute(String dbData) throws InvalidValueException {
        if(Objects.isNull(dbData) || dbData.isEmpty()) {
            return List.of();
        } else {
            final var times = new ArrayList<ParticipleTime>();
            for(var time : dbData.split(delimiter())) {
                times.add(ParticipleTime.of(time));
            }
            return times;
        }
    }

    private static String delimiter() {
        return ",";
    }
}