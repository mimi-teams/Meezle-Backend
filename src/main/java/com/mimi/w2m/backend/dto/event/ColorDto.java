package com.mimi.w2m.backend.dto.event;

import com.mimi.w2m.backend.config.exception.InvalidValueException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.io.Serializable;

/**
 * ColorDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema(title = "색상 정보", type = "String", example = "#FFFFFFFF", description = "이벤트 색상 정보", pattern = "^#[0-9a-f]{6}$")
public class ColorDto implements Serializable {
    @Min(0)
    @Max(255)
    @NotNull
    private Integer red;
    @Min(0)
    @Max(255)
    @NotNull
    private Integer green;
    @Min(0)
    @Max(255)
    @NotNull
    private Integer blue;

    @SuppressWarnings("unused")
    @Builder
    public ColorDto(Integer red, Integer blue, Integer green) {
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    @SuppressWarnings("unused")
    protected ColorDto() {
    }

    public static ColorDto of(String string) {
        if (!string.matches("^#[0-9a-fA-F]{6}$")) {
            throw new InvalidValueException("", "올바른 색상정보가 아닙니다");
        }

        final int red = Integer.parseInt(string.substring(1, 3), 16);
        final int blue = Integer.parseInt(string.substring(3, 5), 16);
        final int green = Integer.parseInt(string.substring(5, 7), 16);

        return ColorDto.builder()
                .red(red)
                .blue(blue)
                .green(green)
                .build();
    }

    public static ColorDto of(Color color) {
        return ColorDto.builder()
                .red(color.getRed())
                .green(color.getGreen())
                .blue(color.getBlue())
                .build();
    }

    public Color to() {
        return new Color(red, blue, green);
    }

    @Override
    public String toString() {
        return String.format("#%2X%2X%2X", red, blue, green);
    }
}