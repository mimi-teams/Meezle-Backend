package com.mimi.w2m.backend.e2eTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mimi.w2m.backend.W2mApplication;
import com.mimi.w2m.backend.config.interceptor.JwtHandler;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.participant.guest.GuestCreateDto;
import com.mimi.w2m.backend.dto.participant.guest.GuestLoginRequest;
import com.mimi.w2m.backend.dto.participant.guest.GuestLoginResponse;
import com.mimi.w2m.backend.service.GuestService;
import com.mimi.w2m.backend.testFixtures.DatabaseCleaner;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * End2End Test 최상위 함수
 *
 * @author paul
 * @since 2022-12-13
 */

@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest(classes = W2mApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
/**
 * Context Loader 를 이용하는 테스트의 경우, 각 Context 가 공유되어 재사용된다. 따라서 격리를 위해, Context를 재생성할 필요가 있다.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public abstract class End2EndTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @Autowired
    protected JwtHandler jwtHandler;

    @Autowired
    protected GuestService guestService;
    @Autowired
    protected CacheManager cacheManager;

    @AfterEach
    protected void cleanup() {
        databaseCleaner.cleanUp();
    }

    /**
     * 로그인 처리
     *
     * @param user 로그인할 유저
     * @return token
     */
    protected String login(User user) {
        return jwtHandler.createToken(user.getId(), Role.USER);
    }

    protected String loginGuest(GuestCreateDto guestCreateDto) {
        guestService.create(guestCreateDto);
        final GuestLoginResponse loginResponse = guestService.login(guestCreateDto.getEventId(), GuestLoginRequest.builder()
                .name(guestCreateDto.getName())
                .password(guestCreateDto.getPassword())
                .build());

        return loginResponse.token();
    }
}
