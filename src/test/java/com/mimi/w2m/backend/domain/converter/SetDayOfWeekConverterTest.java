package com.mimi.w2m.backend.domain.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * SetDayOfWeekConverterTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/24
 **/
class SetDayOfWeekConverterTest {
private final SetDayOfWeekConverter converter = new SetDayOfWeekConverter();

@DisplayName("SetDayOfWeek TYPE -> SetDayOfWeek STR 변환")
@Test
void convertToDatabaseColumn() {
    //given
    final var expectedDayOfWeekString = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY";

    //when
    final var givenDayOfWeekString = converter.convertToDatabaseColumn(Set.of(DayOfWeek.values()));

    //then
    assertThat(expectedDayOfWeekString).contains(givenDayOfWeekString.split(","));
}

@DisplayName("SetDayOfWeek STR -> SetDayOfWeek TYPE 변환")
@Test
void convertToEntityAttribute() {
    //given
    final var expectedDayOfWeek = Set.of(DayOfWeek.values());

    //when
    final var givenDayOfWeek = converter.convertToEntityAttribute("MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY," +
                                                                  "SUNDAY");

    //then
    assertThat(expectedDayOfWeek.equals(givenDayOfWeek)).isTrue();
}
}