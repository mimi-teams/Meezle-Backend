package com.mimi.w2m.backend.domain.type;

import com.mimi.w2m.backend.type.common.ParticipleTime;
import com.mimi.w2m.backend.type.response.exception.InvalidValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * ParticipleTimeTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/24
 **/
class ParticipleTimeTest {
@DisplayName("ParticipleTime 의 유효 범위 확인하기")
@Test
void validRange() {
    //given
    final var expectedValidParticipleTimeString = "00:00:00-11:11:11";

    //when
    final var givenValidParticipleTime = ParticipleTime.of(expectedValidParticipleTimeString);

    //then
    assertThat(givenValidParticipleTime).isNotNull();
}

@DisplayName("ParticipleTime 의 잘못된 값 확인하기")
@Test
void invalidRange() {
    //given
    final var expectedInvalidParticipleTimeString = "11:11:11-00:00:00";

    //when
    assertThatThrownBy(() -> ParticipleTime.of(expectedInvalidParticipleTimeString))
            .isInstanceOf(InvalidValueException.class);
}
}