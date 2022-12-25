package com.mimi.w2m.backend.dto.participant.guest;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.Guest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

/**
 * GuestRequestDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema(title = "가입하지 않은 이용자의 요청 정보", description = "이용자의 이름과 비밀번호, 연관된 이벤트 ID를 받음",
        requiredProperties = {"name", "password", "eventId"},
        example = "{\"name\":\"guest\",\"password\":\"0308\",\"eventId\":0}")
public class GuestRequestDto implements Serializable {
    @Schema(type = "String", description = "이용자 이름", maxLength = 20)
    @Size(min = 1, max = 20)
    @NotNull
    private String name;

    @Schema(type = "String", description = "password 는 Null 이어도 된다", maxLength = 20)
    @Nullable
    private String password;

    @Schema(type = "Integer", description = "이용자가 속한 Event 의 ID")
    @NotNull
    private UUID eventId;

    @Builder
    public GuestRequestDto(String name, String password, UUID eventId) {
        this.name = name;
        this.password = password;
        this.eventId = eventId;
    }

    protected GuestRequestDto() {
    }

    public Guest to(Event event, String salt, String hashedPw) {
        return Guest.builder()
                .name(name)
                .password(hashedPw)
                .event(event)
                .build();
    }
}