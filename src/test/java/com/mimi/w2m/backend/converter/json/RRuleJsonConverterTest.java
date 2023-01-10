package com.mimi.w2m.backend.converter.json;

import com.mimi.w2m.backend.client.kakao.dto.calendar.event.RRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * RRuleJsonConverterTest
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/10
**/
class RRuleJsonConverterTest {
    @Test
    @DisplayName("Serializer Test")
    void testSerializer() {
        //given
        final var rrule = new RRule(RRule.FreqType.WEEKLY, List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));
        final var expected = "FREQ=WEEKLY;BYDAY=MO,TU,WE,TH,FR,SA,SU;";
        //when
        final var ret = new StringBuilder();
        ret.append(String.format("FREQ=%s;", rrule.freq().getValue()));
        final var builder = new StringBuilder();
        rrule.byDay().forEach(day -> {
            switch (day) {
                case SUNDAY -> builder.append("SU,");
                case MONDAY -> builder.append("MO,");
                case TUESDAY -> builder.append("TU,");
                case WEDNESDAY -> builder.append("WE,");
                case THURSDAY -> builder.append("TH,");
                case FRIDAY -> builder.append("FR,");
                case SATURDAY -> builder.append("SA,");
            }
        });
        builder.replace(builder.lastIndexOf(","), builder.lastIndexOf(",") + 1, ";");
        ret.append(String.format("BYDAY=%s", builder));

        //then
        assertThat(ret.toString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Deserializer Test")
    void testDeserializer() {
        //given
        final var raw = "FREQ=WEEKLY;BYDAY=MO,TU,WE,TH,FR,SA,SU;";
        final var expected = new RRule(RRule.FreqType.WEEKLY, List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));

        //when
        final var splited = raw.split(";");
        RRule.FreqType freq = null;
        ArrayList<DayOfWeek> byDay = new ArrayList<>();
        for (var str:
                splited) {
            final var token = str.split("=");
            switch(token[0]) {
                case "FREQ" -> {
                    switch (token[1]) {
                        case "WEEKLY" -> freq = RRule.FreqType.WEEKLY;
                    }
                }
                case "BYDAY" -> {
                    Arrays.stream(token[1].split(",")).forEach(day -> {
                        switch(day) {
                            case "SU" -> byDay.add(DayOfWeek.SUNDAY);
                            case "MO" -> byDay.add(DayOfWeek.MONDAY);
                            case "TU" -> byDay.add(DayOfWeek.TUESDAY);
                            case "WE" -> byDay.add(DayOfWeek.WEDNESDAY);
                            case "TH" -> byDay.add(DayOfWeek.THURSDAY);
                            case "FR" -> byDay.add(DayOfWeek.FRIDAY);
                            case "SA" -> byDay.add(DayOfWeek.SATURDAY);
                        }
                    });
                }
            }
        }
        final var ret = new RRule(freq, byDay);
        //then
        assertThat(ret.freq()).isEqualTo(expected.freq());
        assertThat(ret.byDay()).containsExactlyElementsOf(expected.byDay());
    }

}