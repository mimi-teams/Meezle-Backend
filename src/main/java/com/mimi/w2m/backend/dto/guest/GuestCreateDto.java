package com.mimi.w2m.backend.dto.guest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 게스트 생성에 사용되는 DTO
 * @since 2022-12-17
 * @author yeh35
 */
@Getter
public class GuestCreateDto implements Serializable {
    @Schema(type = "String", description = "이용자 이름", maxLength = 20)
    @Size(min = 1, max = 20)
    @NotNull
    private String name;
    @Schema(type = "String", description = "password 는 Null 이어도 된다", maxLength = 20)
    @Nullable
    private String password;
    @Schema(type = "Integer", description = "이용자가 속한 Event 의 ID")
    @NotNull
    @PositiveOrZero
    private Long eventId;

    @SuppressWarnings("unused")
    @Builder
    public GuestCreateDto(String name, String password, Long eventId) {
        this.name = name;
        this.password = password;
        this.eventId = eventId;
    }


    @SuppressWarnings("unused")
    protected GuestCreateDto() {
    }

}