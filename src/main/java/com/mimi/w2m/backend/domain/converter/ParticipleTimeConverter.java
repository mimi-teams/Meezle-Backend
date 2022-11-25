package com.mimi.w2m.backend.domain.converter;

import com.mimi.w2m.backend.domain.type.ParticipleTime;
import com.mimi.w2m.backend.error.InvalidValueException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter
public class ParticipleTimeConverter implements AttributeConverter<ParticipleTime, String> {
@Override
public String convertToDatabaseColumn(ParticipleTime attribute) {
    if(Objects.isNull(attribute)) {
        return null;
    } else {
        return attribute.toString();
    }
}

/**
 * db에 저장되는 것은 이미 검증이 끝난 데이터이다
 *
 * @author teddy
 * @since 2022/11/22
 **/
@Override
public ParticipleTime convertToEntityAttribute(String dbData) throws InvalidValueException {
    if(Objects.isNull(dbData)) {
        return null;
    } else {
        return ParticipleTime.of(dbData);
    }
}
}