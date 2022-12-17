package com.mimi.w2m.backend.testFixtures;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.dto.guest.GuestCreateDto;

public class GuestTestFixture {


    public static GuestCreateDto createGuestCreateDto(Event event) {
        return GuestCreateDto.builder()
                .name("테스트 게스트")
                .password("1234")
                .eventId(event.getId())
                .build();
    }


}
