package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.config.cache.SpringCacheConfig;
import com.mimi.w2m.backend.repository.GuestRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

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
}