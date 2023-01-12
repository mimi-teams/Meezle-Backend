package com.mimi.w2m.backend;

import com.mimi.w2m.backend.api.v1.UserApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

@EnableFeignClients
@SpringBootApplication
public class W2mApplication {

    private final Logger logger = LoggerFactory.getLogger(UserApi.class.getName());

    @PostConstruct
    public void started() {
        // timezone UTC 셋팅
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        logger.info("start time");
        logger.info("ZonedDateTime now : " + ZonedDateTime.now());
        logger.info("LocalDateTime now : " + LocalDateTime.now());
    }

    public static void main(String[] args) {
        SpringApplication.run(W2mApplication.class, args);
    }

}
