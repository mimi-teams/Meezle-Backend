package com.mimi.w2m.backend.e2eTest.api.v1;

import com.mimi.w2m.backend.e2eTest.End2EndTest;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.repository.GuestRepository;
import com.mimi.w2m.backend.repository.UserRepository;
import com.mimi.w2m.backend.testFixtures.EventTestFixture;
import com.mimi.w2m.backend.testFixtures.GuestTestFixture;
import com.mimi.w2m.backend.testFixtures.UserTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * LandingApiTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/18
 **/
public class LandingApiTest extends End2EndTest {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected GuestRepository guestRepository;
    @Autowired
    protected EventRepository eventRepository;

    @Test
    @DisplayName("landing Page Api Test")
    void testCache() throws Exception {
        final var user = userRepository.save(UserTestFixture.createUser());
        final var event = eventRepository.save(EventTestFixture.createEvent(user));
        final var guest = guestRepository.save(GuestTestFixture.createGuest(event));

        mockMvc.perform(
                        get("/v1/landing")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.eventCount").value(1))
                .andExpect(jsonPath("$.data.userCount").value(2));
    }
}