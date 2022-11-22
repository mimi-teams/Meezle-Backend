package com.mimi.w2m.backend.domain.converter;

import com.mimi.w2m.backend.domain.type.ParticipleTime;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SetParticipleTimeConverterTest {
private final SetParticipleTimeConverter converter = new SetParticipleTimeConverter();

@Test
void convertToDatabaseColumn() {
    //given
    final var expectedParticipleTimeString = "00:00:00-01:01:01,01:01:01-02:02:02,";

    //when
    final var givenParticipleTimeString = converter.convertToDatabaseColumn(Set.of(ParticipleTime.of("00:00:00-01:01" +
                                                                                                     ":01").get(),
                                                                                   ParticipleTime.of("01:01:01-02:02" +
                                                                                                     ":02").get()));

    //then
    assertThat(expectedParticipleTimeString).contains(givenParticipleTimeString.split(","));
}

@Test
void convertToEntityAttribute() {
    //given
    final var expectedParticipleTime = Set.of(ParticipleTime.of("00:00:00-01:01:01").get(),
                                              ParticipleTime.of("01:01:01-02:02:02").get());

    //when
    final var givenParticipleTime = converter.convertToEntityAttribute("00:00:00-01:01:01,01:01:01-02:02:02,");

    //then
    assertThat(expectedParticipleTime).isEqualTo(givenParticipleTime);
}
}