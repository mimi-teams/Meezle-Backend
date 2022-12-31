package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.repository.BlockedJwtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * SchedulerService : GCP 의 Scheduling 을 사용한다(API 호출!)
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/12/29
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BatchService {
    private final BlockedJwtRepository blockedJwtRepository;

    /**
     * Logout 된 Jwt Token 을 삭제하기
     *
     * @author teddy
     * @since 2022/12/28
     **/
    @Transactional
    public void cleanLogoutJwtTokens() {
        final var threshold = LocalDateTime.now().minus(12, ChronoUnit.HOURS);
        blockedJwtRepository.deleteByExpiredDateBefore(threshold);
    }
}