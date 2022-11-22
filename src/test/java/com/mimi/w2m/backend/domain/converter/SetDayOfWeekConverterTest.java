package com.mimi.w2m.backend.domain.converter;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SetDayOfWeekConverterTest {
private final SetDayOfWeekConverter converter = new SetDayOfWeekConverter();

@Test
void convertToDatabaseColumn() {
    //given
    final var expectedDayOfWeek = Set.of(DayOfWeek.values());

    //when
    final var givenDayOfWeek = converter.convertToEntityAttribute("MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY," +
                                                                 "SUNDAY");

    //then
    assertThat(expectedDayOfWeek.equals(givenDayOfWeek)).isTrue();
}

@Test
void convertToEntityAttribute() {
    //given
    final var expectedDayOfWeekString = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY";

    //when
    final var givenDayOfWeekString = converter.convertToDatabaseColumn(Set.of(DayOfWeek.values()));

    //then
    assertThat(expectedDayOfWeekString).contains(givenDayOfWeekString.split(","));
}
}