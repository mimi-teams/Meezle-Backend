package com.mimi.w2m.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class W2mApplication {

	public static void main(String[] args) {
		SpringApplication.run(W2mApplication.class, args);
	}

}
