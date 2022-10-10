package com.mimi.w2m.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class W2mApplicationTest {
	@LocalServerPort
	private Long port;

	@Test
	void contextLoads() {
	}

}