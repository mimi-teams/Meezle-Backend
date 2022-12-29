package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.config.db.SpringDbConfig;
import com.mimi.w2m.backend.domain.BlockedJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BlockedJwtRepositoryTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/12/29
 **/
@ActiveProfiles("test")
@Import(SpringDbConfig.class) // For auditing
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BlockedJwtRepositoryTest {
    @Autowired
    private BlockedJwtRepository blockedJwtRepository;

    @Test
    @DisplayName("스케쥴러에 의한 JWT Token 삭제 확인 테스트")
    void deleteAllByScheduler() throws InterruptedException {
        //given
        var token1 = "jwt1";
        var token2 = "jwt2";
        blockedJwtRepository.save(new BlockedJwt(token1));
        //when
        Thread.sleep(2000);
        blockedJwtRepository.save(new BlockedJwt(token2));
        blockedJwtRepository.deleteByCreatedDateBefore(LocalDateTime.now().minus(1000, ChronoUnit.MILLIS));
        //then
        assertThat(blockedJwtRepository.findById(token1)).isEmpty();
        assertThat(blockedJwtRepository.findById(token2)).isPresent();
    }
}