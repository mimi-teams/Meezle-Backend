package com.mimi.w2m.backend.dto.event;

import com.mimi.w2m.backend.type.dto.event.ColorDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorDtoTest {

    @Test
    void test() {
        final var colorString = "#FFEECC11";

        ColorDto colorDto = ColorDto.of(colorString);

        assertEquals(Integer.parseInt("FF", 16), colorDto.getRed());
        assertEquals(Integer.parseInt("EE", 16), colorDto.getBlue());
        assertEquals(Integer.parseInt("CC", 16), colorDto.getGreen());
        assertEquals(Integer.parseInt("11", 16), colorDto.getAlpha());
        assertEquals(colorString, colorDto.toString());
    }

}