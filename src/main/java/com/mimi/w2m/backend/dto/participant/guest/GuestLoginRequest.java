package com.mimi.w2m.backend.dto.participant.guest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Guest Entity 와 1 대 1 매칭된다.
 * @since 2022-12-17
 * @author yeh35
 */
@Getter
@Schema(title = "이벤트 게스트", description = "이용자의 ID 와 Name, 연관된 이벤트 ID를 보냄")
public class GuestLoginRequest implements Serializable {

    @Schema(description = "게스트 이름", maxLength = 20)
    @Size(min = 1, max = 20)
    @NotNull
    private String name;

    @Schema(description = "게스트 비밀번호", maxLength = 20)
    @Size(min = 1, max = 30)
    @NotNull
    private String password;


    @SuppressWarnings("unused")
    @Builder
    public GuestLoginRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }


    @SuppressWarnings("unused")
    protected GuestLoginRequest() {
    }
}