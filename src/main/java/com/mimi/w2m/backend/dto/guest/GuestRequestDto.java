package com.mimi.w2m.backend.dto.guest;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.Guest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * GuestRequestDto
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/17
 **/
@Getter
@Schema
public class GuestRequestDto implements Serializable {
private                                                  String name;
@Schema(description = "password 는 입력하지 않아도 됩니다") private String password;
private                                                  Long   eventId;

@Builder
public GuestRequestDto(String name, String password, Long eventId) {
    this.name     = name;
    this.password = password;
    this.eventId  = eventId;
}

protected GuestRequestDto() {
}

public Guest to(Event event, String salt, String hashedPw) {
    return Guest
                   .builder()
                   .name(name)
                   .salt(salt)
                   .password(hashedPw)
                   .event(event)
                   .build();
}
}