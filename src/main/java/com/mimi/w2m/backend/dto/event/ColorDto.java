package com.mimi.w2m.backend.dto.event;

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
@Schema(description = "Color를 받기 위한 dto")
public class ColorDto implements Serializable {
private Integer red;
private Integer green;
private Integer blue;
private Integer alpha;

// TODO: 2022/11/27 RGB를 #rrbbgg 형식으로 주고받기
@Builder
public ColorDto(Integer red, Integer green, Integer blue, Integer alpha) {
    this.red   = red;
    this.green = green;
    this.blue  = blue;
    this.alpha = alpha;
}

protected ColorDto() {
}

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