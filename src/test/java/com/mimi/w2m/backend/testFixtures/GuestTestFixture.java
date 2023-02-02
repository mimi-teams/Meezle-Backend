package com.mimi.w2m.backend.testFixtures;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.Guest;
import com.mimi.w2m.backend.dto.participant.guest.GuestCreateDto;

public class GuestTestFixture {


    public static GuestCreateDto createGuestCreateDto(Event event) {
        return GuestCreateDto.builder()
                .name("테스트 게스트")
                .password("1234")
                .eventId(event.getId())
                .build();
    }


    public static Guest createGuest(Event event) {
        return Guest.builder()
                .name("testGuest")
                .event(event)
                .build();
    }
}
