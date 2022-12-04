package com.mimi.w2m.backend.converter.db;

import com.mimi.w2m.backend.common.Role;
import com.mimi.w2m.backend.exception.InvalidValueException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Formatter;

/**
 * RoleConverter
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Converter
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role attribute) {
        return attribute.getKey();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) throws InvalidValueException {
        if (dbData.equals(Role.GUEST.getKey())) {
            return Role.GUEST;
        } else if (dbData.equals(Role.USER.getKey())) {
            return Role.USER;
        } else {
            final var formatter = new Formatter();
            final var msg = formatter.format("Invalid Role String : %s", dbData)
                    .toString();
            throw new InvalidValueException(msg);
        }
    }
}