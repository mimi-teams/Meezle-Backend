package com.mimi.w2m.backend.converter.db;

import com.mimi.w2m.backend.domain.type.LoginPlatformType;

import javax.persistence.AttributeConverter;

/**
 * OAuth2PlatformTypeConverter
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/08
**/
public class OAuth2PlatformTypeConverter implements AttributeConverter<LoginPlatformType, String> {
    @Override
    public String convertToDatabaseColumn(LoginPlatformType attribute) {
        return attribute.getKey();
    }

    @Override
    public LoginPlatformType convertToEntityAttribute(String dbData) {
        return LoginPlatformType.valueOf(dbData);
    }
}