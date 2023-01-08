package com.mimi.w2m.backend;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeTest {

    void timetest() {

        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
        zonedDateTime.toLocalTime();
    }

}
