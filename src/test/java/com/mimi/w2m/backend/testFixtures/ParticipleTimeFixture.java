package com.mimi.w2m.backend.testFixtures;

import com.mimi.w2m.backend.domain.type.ParticipleTime;

import java.util.List;

public class ParticipleTimeFixture {

    public static List<ParticipleTime> createParticipleTime() {
        return List.of(
                ParticipleTime.of("MONDAY[T]10:00:00-12:00:00|13:00:00-14:00:00|"),
                ParticipleTime.of("TUESDAY[T]10:00:00-12:00:00|13:00:00-14:00:00|"),
                ParticipleTime.of("THURSDAY[T]10:00:00-12:00:00|13:00:00-14:00:00|")
        );
    }

}
