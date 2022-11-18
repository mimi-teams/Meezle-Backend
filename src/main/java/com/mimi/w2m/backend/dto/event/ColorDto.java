package com.mimi.w2m.backend.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.awt.*;
import java.io.Serializable;

/**
 * ColorDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Data
@Builder
@Schema(description = "Color를 받기 위한 dto")
public class ColorDto implements Serializable {
private final Integer red;
private final Integer green;
private final Integer blue;
private final Integer alpha;

public static ColorDto of(Color color) {
    return ColorDto.builder()
                   .red(color.getRed())
                   .green(color.getGreen())
                   .blue(color.getBlue())
                   .alpha(color.getAlpha())
                   .build();
}

public Color to() {
    return new Color(red, green, blue, alpha);
}
}