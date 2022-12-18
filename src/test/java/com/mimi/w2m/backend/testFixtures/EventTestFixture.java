package com.mimi.w2m.backend.testFixtures;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.EventSelectableParticipleTime;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
import com.mimi.w2m.backend.dto.event.ColorDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class EventTestFixture {

    public static Event createEvent(User user) {
        return Event.builder()
                .title("테스트 이벤트")
                .dDay(LocalDateTime.now().plusDays(5))
                .color(ColorDto.of("#ef2334"))
                .description("테스트용 이벤트입니다.")
                .host(user)
                .build();
    }

}
