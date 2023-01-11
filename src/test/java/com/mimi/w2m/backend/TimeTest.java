package com.mimi.w2m.backend;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.TimeZone;

public class TimeTest {

    @Test
    void timetest() {
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
//        Locale.setDefault(Locale.KOREA);

        System.out.println("start time");
        System.out.println("ZonedDateTime now : " + ZonedDateTime.now());
        System.out.println("LocalDateTime now : " + LocalDateTime.now());
    }

}
