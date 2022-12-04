package com.mimi.w2m.backend.type.dto.guest;

import com.mimi.w2m.backend.type.domain.Guest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * GuestResponseDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema(title = "가입하지 않은 이용자의 반환 정보", description = "이용자의 ID 와 Name, 연관된 이벤트 ID를 보냄",
        requiredProperties = {"id", "name", "eventId"}, example = "{\"id\":0, \"name\":\"guest\", \"eventId\":1}")
public class GuestResponseDto implements Serializable {
    @Schema(type = "Integer", description = "이용자 ID")
    @NotNull
    @PositiveOrZero
    private Long id;
    @Schema(type = "Integer", description = "이용자가 속한 Event 의 ID")
    @NotNull
    @PositiveOrZero
    private Long eventId;
    @Schema(type = "String", description = "이용자 이름", maxLength = 20)
    @Size(min = 1, max = 20)
    @NotNull
    private String name;

    @Builder
    public GuestResponseDto(Long id, Long eventId, String name) {
        this.id = id;
        this.eventId = eventId;
        this.name = name;
    }

    protected GuestResponseDto() {
    }

    public static GuestResponseDto of(Guest entity) {
        return GuestResponseDto.builder()
                .id(entity.getId())
                .eventId(entity.getEvent()
                        .getId())
                .name(entity.getName())
                .build();
    }

}