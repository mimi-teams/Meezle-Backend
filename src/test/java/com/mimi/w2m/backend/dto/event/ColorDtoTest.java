package com.mimi.w2m.backend.dto.event;

import org.junit.jupiter.api.DisplayName;
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
        assertEquals(colorString, colorDto.toString());
    }

    @DisplayName("모든 색상 테스트")
    @Test
    void test2() {

        // 모든 색상
        for (int r = 0; r < 255; r++) {
            for (int g = 0; g < 255; g++) {
                for (int b = 0; b < 255; b++) {
                    final String red = String.format("%02x", r) ;
                    final String green = String.format("%02x", g);
                    final String blue = String.format("%02x", b);
                    final var colorString = String.format("#%s%s%s", red, green, blue);

                    ColorDto colorDto = ColorDto.of(colorString);
                    assertEquals(r, colorDto.getRed());
                    assertEquals(g, colorDto.getBlue());
                    assertEquals(b, colorDto.getGreen());
                    assertEquals(colorString, colorDto.toString());
                }
            }
        }
    }

}