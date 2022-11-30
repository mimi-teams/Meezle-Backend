package com.mimi.w2m.backend.type.converter.db;

import com.mimi.w2m.backend.type.common.Role;

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
public class RoleConverter implements AttributeConverter<Role, String> {

@Override
public String convertToDatabaseColumn(Role attribute) {
    return attribute.getKey();
}

@Override
public Role convertToEntityAttribute(String dbData) {
    if(dbData.equals(Role.GUEST.getKey())) {
        return Role.GUEST;
    } else if(dbData.equals(Role.USER.getKey())) {
        return Role.USER;
    } else {
        return Role.NONE;
    }
}
}