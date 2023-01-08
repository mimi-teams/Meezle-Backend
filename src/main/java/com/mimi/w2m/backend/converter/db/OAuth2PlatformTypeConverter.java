package com.mimi.w2m.backend.converter.db;

import com.mimi.w2m.backend.domain.type.OAuth2PlatformType;

import javax.persistence.AttributeConverter;

/**
 * OAuth2PlatformTypeConverter
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/08
**/
public class OAuth2PlatformTypeConverter implements AttributeConverter<OAuth2PlatformType, String> {
    @Override
    public String convertToDatabaseColumn(OAuth2PlatformType attribute) {
        return attribute.getKey();
    }

    @Override
    public OAuth2PlatformType convertToEntityAttribute(String dbData) {
        return OAuth2PlatformType.valueOf(dbData);
    }
}