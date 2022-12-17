package com.mimi.w2m.backend.converter.db;

import com.mimi.w2m.backend.config.exception.InvalidValueException;
import com.mimi.w2m.backend.dto.event.ColorDto;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * RoleConverter
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Converter
public class ColorConverter implements AttributeConverter<ColorDto, String> {

    @Override
    public String convertToDatabaseColumn(ColorDto attribute) {
        return attribute.toString();
    }

    @Override
    public ColorDto convertToEntityAttribute(String dbData) throws InvalidValueException {
        return ColorDto.of(dbData);
    }
}