package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.dto.landing.LandingResponseDto;
import com.mimi.w2m.backend.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * LandingService
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/20
 **/
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LandingService {
    private final EventRepository eventRepository;

    @Cacheable(cacheNames = "landingInfo")
    public LandingResponseDto getLandingData() {
        final var eventCnt = eventRepository.count();
        return LandingResponseDto.builder()
                .eventCount(eventCnt)
                .build();
    }
}