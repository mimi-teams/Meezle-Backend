package com.mimi.w2m.backend.domain.converter;

import com.mimi.w2m.backend.domain.type.ParticipleTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SetParticipleTimeConverter : beginTime-endTime, ... 형식으로 주고받는다 time 은 hh:mm:ss 형식!
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/19
 **/
@Converter
public class SetParticipleTimeConverter implements AttributeConverter<Set<ParticipleTime>, String> {

@Override
public String convertToDatabaseColumn(Set<ParticipleTime> attribute) {
    var builder = new StringBuilder();
    for(var participleTime :
            attribute) {
        builder.append(participleTime);
        builder.append(',');
    }
    return builder.toString();
}

@Override
public Set<ParticipleTime> convertToEntityAttribute(String dbData) {
    return Arrays.stream(dbData.split(","))
                 .map(ParticipleTime::of)
                 .collect(Collectors.toSet());
}
}