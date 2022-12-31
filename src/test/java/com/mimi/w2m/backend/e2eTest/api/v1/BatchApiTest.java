package com.mimi.w2m.backend.e2eTest.api.v1;

import com.mimi.w2m.backend.e2eTest.End2EndTest;
import com.mimi.w2m.backend.repository.BlockedJwtRepository;
import com.mimi.w2m.backend.testFixtures.BlockedJwtTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings({"unused"})
class BatchApiTest extends End2EndTest {
    @Autowired
    protected BlockedJwtRepository blockedJwtRepository;

    @Test
    @DisplayName("로그아웃된 Jwt 삭제 Api 테스트")
    void clean() throws Exception {
        //given
        final var blockedJwt1 = BlockedJwtTestFixture.createBlockedJwt(
                "token1",
                LocalDateTime.now().minus(12, ChronoUnit.HOURS)
        );
        final var blockedJwt2 = BlockedJwtTestFixture.createBlockedJwt(
                "token2",
                LocalDateTime.now()
        );
        blockedJwtRepository.save(blockedJwt1);
        blockedJwtRepository.save(blockedJwt2);

        //when&then
        mockMvc.perform(
                        post("/v1/batch/clean?data=LOGOUT_TOKEN")
                )
                .andExpect(status().isOk());
        assertThat(blockedJwtRepository.findById("token1")).isEmpty();
        assertThat(blockedJwtRepository.findById("token2")).isPresent();
    }
}