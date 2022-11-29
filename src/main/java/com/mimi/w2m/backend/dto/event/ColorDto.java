package com.mimi.w2m.backend.dto.event;

import com.mimi.w2m.backend.error.InvalidValueException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

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
@Schema(type = "String", example = "#FFFFFFFF", description = "Color를 받기 위한 dto")
public class ColorDto implements Serializable {
    private Integer red;
    private Integer green;
    private Integer blue;
    private Integer alpha;

    @Builder
    public ColorDto(Integer red, Integer blue, Integer green, Integer alpha) {
        this.red   = red;
        this.blue  = blue;
        this.green = green;
        this.alpha = alpha;
    }

    protected ColorDto() {
    }

    public Color to() {
        return new Color(red, blue, green, alpha);
    }

    @Override
    public String toString() {
        return String.format("#%2X%2X%2X%2X", red, blue, green, alpha);
    }

    public static ColorDto of(String string) {
        if(string.charAt(0) != '#') {
            throw new InvalidValueException("", "Color이 아니다. 가장 앞에 #이 들어가야한다.");
        }

        final int red   = Integer.parseInt(string.substring(1, 3), 16);
        final int blue  = Integer.parseInt(string.substring(3, 5), 16);
        final int green = Integer.parseInt(string.substring(5, 7), 16);
        final int alpha = Integer.parseInt(string.substring(7, 9), 16);

        return ColorDto.builder().red(red).blue(blue).green(green).alpha(alpha).build();
    }

    public static ColorDto of(Color color) {
        return ColorDto.builder().red(color.getRed()).green(color.getGreen()).blue(color.getBlue()).alpha(color.getAlpha()).build();
    }
}