package com.mimi.w2m.backend.converter.db;

import com.mimi.w2m.backend.domain.type.LoginPlatformType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * LoginPlatformTypeConverterTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/08
 **/
class LoginPlatformTypeConverterTest {

    @Test
    @DisplayName("Enum Type -> String")
    void convertToDatabaseColumn() {
        //given
        final var converter = new OAuth2PlatformTypeConverter();
        final var type = LoginPlatformType.KAKAO;
        //when & then
        assertThat(converter.convertToDatabaseColumn(type)).isEqualTo("KAKAO");
    }

    @Test
    @DisplayName("String -> Enum Type")
    void convertToEntityAttribute() {
        //given
        final var converter = new OAuth2PlatformTypeConverter();

        //when&then
        assertThat(converter.convertToEntityAttribute("KAKAO")).isEqualTo(LoginPlatformType.KAKAO);
    }
}