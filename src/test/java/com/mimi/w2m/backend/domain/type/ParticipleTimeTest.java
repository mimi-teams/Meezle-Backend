package com.mimi.w2m.backend.domain.type;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ParticipleTimeTest {
@Test
void validRange() {
    //given
    final var expectedValidParticipleTimeString = "00:00:00-11:11:11";

    //when
    final var givenValidParticipleTime = ParticipleTime.of(expectedValidParticipleTimeString);

    //then
    assertThat(givenValidParticipleTime.isPresent()).isTrue();
}

@Test
void invalidRange() {
    //given
    final var expectedInvalidParticipleTimeString = "11:11:11-00:00:00";

    //when
    final var givenInvalidParticipleTime = ParticipleTime.of(expectedInvalidParticipleTimeString);

    //then
    assertThat(givenInvalidParticipleTime).isEmpty();
}
}