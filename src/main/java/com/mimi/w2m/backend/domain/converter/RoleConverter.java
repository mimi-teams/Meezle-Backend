package com.mimi.w2m.backend.domain.converter;

import com.mimi.w2m.backend.domain.type.Role;

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
    if(dbData.equals(Role.PARTICIPANT.getKey())) {
        return Role.PARTICIPANT;
    } else if(dbData.equals(Role.USER.getKey())) {
        return Role.USER;
    } else {
        return Role.Tester;
    }
}
}