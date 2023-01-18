package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.config.cache.SpringCacheConfig;
import com.mimi.w2m.backend.repository.GuestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * GuestServiceTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/24
 **/
@ExtendWith(MockitoExtension.class)
@Import(SpringCacheConfig.class)
class GuestServiceTest {
    @Mock
    private GuestRepository guestRepository;
    @InjectMocks
    private GuestService guestService;

    @Test
    @DisplayName("Cache Test")
    void testCache() {
        given(guestRepository.count()).willReturn(1L);
        for (int i = 0; i < 100; i++) {
            guestService.getTotal();
        }
        then(guestRepository).should(times(1)).count();
    }

}