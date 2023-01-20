package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.dto.landing.LandingResponseDto;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.repository.GuestRepository;
import com.mimi.w2m.backend.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final GuestRepository guestRepository;

    @Cacheable(cacheNames = "landingInfo")
    public LandingResponseDto getLandingData() {
        final var userCnt = userRepository.count();
        final var guestCnt = guestRepository.count();
        final var eventCnt = eventRepository.count();
        return LandingResponseDto.builder()
                .userCount(userCnt + guestCnt)
                .eventCount(eventCnt)
                .build();
    }
}