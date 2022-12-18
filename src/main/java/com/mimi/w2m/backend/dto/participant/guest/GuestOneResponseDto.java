package com.mimi.w2m.backend.dto.participant.guest;

import com.mimi.w2m.backend.domain.Guest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.io.Serializable;

/**
 * @since 2022-12-17
 * @author yeh35
 */
@Getter
@Schema(title = "하나의 게스트 정보만 리턴")
public class GuestOneResponseDto implements Serializable {
    private GuestDto guest;

    @SuppressWarnings("unused")
    protected GuestOneResponseDto() {
    }

    public static GuestOneResponseDto of(Guest entity) {
        final var  responseDto = new GuestOneResponseDto();
        responseDto.guest = GuestDto.of(entity);
        return responseDto;
    }

}