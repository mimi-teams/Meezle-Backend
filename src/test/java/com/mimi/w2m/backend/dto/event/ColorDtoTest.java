package com.mimi.w2m.backend.dto.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT) // 테스트 속도 향상을 위해 동시에 실행
class ColorDtoTest {

    @Test
    void test() {
        final var colorString = "#ffeecc";

        ColorDto colorDto = ColorDto.of(colorString);

        assertEquals(Integer.parseInt("FF", 16), colorDto.getRed());
        assertEquals(Integer.parseInt("EE", 16), colorDto.getBlue());
        assertEquals(Integer.parseInt("CC", 16), colorDto.getGreen());
        assertEquals(colorString, colorDto.toString());
    }

    @DisplayName("모든 색상 테스트(1/4)")
    @Test
    void allColor1() {
        allColor(1);
    }

    @DisplayName("모든 색상 테스트(2/4)")
    @Test
    void allColor2() {
        allColor(2);
    }

    @DisplayName("모든 색상 테스트(3/4)")
    @Test
    void allColor3() {
        allColor(3);
    }

    @DisplayName("모든 색상 테스트(4/4)")
    @Test
    void allColor4() {
        allColor(4);
    }

    private void allColor(int modI) {

        assertTrue(modI <= 4);

        for (int r = modI; r < 255; r = r + 4) {
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