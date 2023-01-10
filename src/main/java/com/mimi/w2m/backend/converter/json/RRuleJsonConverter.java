package com.mimi.w2m.backend.converter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.mimi.w2m.backend.client.kakao.dto.calendar.event.RRule;
import com.mimi.w2m.backend.config.exception.InvalidValueException;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * RRuleJsonConverter
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/10
 **/
@JsonComponent
public class RRuleJsonConverter {
    public static class Serializer extends JsonSerializer<RRule> {

        @Override
        public void serialize(RRule value, JsonGenerator gen, SerializerProvider serializers) throws InvalidValueException {
            try {
                final var builder = new StringBuilder();
                builder.append(String.format("FREQ=%s;", value.freq().getValue()));
                builder.append(("BYDAY="));

                value.byDay().forEach(day -> {
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
                gen.writeString(builder.toString());
            } catch (IOException e) {
                final var msg = "Serializer failed";
                throw new InvalidValueException((msg));
            }
        }
    }

    public static class Deserializer extends JsonDeserializer<RRule> {

        @Override
        public RRule deserialize(JsonParser p, DeserializationContext ctxt) throws InvalidValueException {
            try {
                final var raw = ((JsonNode)p.getCodec().readTree(p)).asText();
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
                return new RRule(freq, byDay);

            } catch (IOException e) {
                final var msg = "Deserializer Failed";
                throw new InvalidValueException(msg);
            }
        }
    }
}