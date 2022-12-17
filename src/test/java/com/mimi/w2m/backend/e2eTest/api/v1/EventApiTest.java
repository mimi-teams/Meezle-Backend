package com.mimi.w2m.backend.e2eTest.api.v1;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.domain.type.ParticipleTime;
import com.mimi.w2m.backend.dto.event.ColorDto;
import com.mimi.w2m.backend.dto.event.EventRequestDto;
import com.mimi.w2m.backend.dto.guest.GuestLoginRequest;
import com.mimi.w2m.backend.e2eTest.End2EndTest;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.repository.UserRepository;
import com.mimi.w2m.backend.service.GuestService;
import com.mimi.w2m.backend.testFixtures.EventTestFixture;
import com.mimi.w2m.backend.testFixtures.GuestTestFixture;
import com.mimi.w2m.backend.testFixtures.UserTestFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
public class EventApiTest extends End2EndTest {

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected EventRepository eventRepository;
    @Autowired
    protected GuestService guestService;

    @Test
    void 이벤트_등록() throws Exception {
        // given
        final User user = UserTestFixture.createUser();
        userRepository.save(user);

        final String token = login(user);
        final var requestDto = EventRequestDto.builder()
                .title("아아 테스트")
                .selectableParticipleTimes(Set.of(
                        ParticipleTime.of("MONDAY[T]10:00:00-12:00:00|13:00:00-14:00:00|"),
                        ParticipleTime.of("TUESDAY[T]10:00:00-12:00:00|13:00:00-14:00:00|"),
                        ParticipleTime.of("THURSDAY[T]10:00:00-12:00:00|13:00:00-14:00:00|")
                ))
                .dDay(null)
                .color(ColorDto.of("#ffffff"))
                .description("테스트입니다람쥐")
                .build();


        //when & then
        mockMvc.perform(
                        post("/v1/events")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.event.id").exists())
                .andExpect(jsonPath("$.data.event.title").value(requestDto.getTitle()))
                .andExpect(jsonPath("$.data.event.selectableParticipleTimes").exists())
                .andExpect(jsonPath("$.data.event.selectedParticipleTimes").exists())
                .andExpect(jsonPath("$.data.event.color").value(requestDto.getColor().toString()))
                .andExpect(jsonPath("$.data.event.dday").exists())
        ;
    }

    @Test
    void 이벤트_조회() throws Exception {
        // given
        final User user = UserTestFixture.createUser();
        userRepository.save(user);

        final Event event = EventTestFixture.createEvent(user);
        eventRepository.save(event);

        //when & then
        mockMvc.perform(
                        get("/v1/events/{id}", event.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.event.id").exists())
                .andExpect(jsonPath("$.data.event.title").value(event.getTitle()))
                .andExpect(jsonPath("$.data.event.selectableParticipleTimes").exists())
                .andExpect(jsonPath("$.data.event.selectedParticipleTimes").exists())
                .andExpect(jsonPath("$.data.event.color").value(event.getColor().toString()))
                .andExpect(jsonPath("$.data.event.dday").exists())
        ;
    }

    @Test
    void 이벤트_수정() throws Exception {
        // given
        final User user = UserTestFixture.createUser();
        userRepository.save(user);

        final String token = login(user);

        final Event event = EventTestFixture.createEvent(user);
        eventRepository.save(event);

        final var requestDto = EventRequestDto.builder()
                .title("수정된 테스트 이벤트")
                .selectableParticipleTimes(Set.of(
                        ParticipleTime.of("SATURDAY[T]10:00:00-12:00:00|13:00:00-14:00:00|"),
                        ParticipleTime.of("SUNDAY[T]10:00:00-12:00:00|13:00:00-14:00:00|")
                ))
                .dDay(null)
                .color(ColorDto.of("#ffffff"))
                .description("수정되었습니다람쥐")
                .build();


        //when & then
        mockMvc.perform(
                        patch("/v1/events/{id}", event.getId())
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.event.id").exists())
                .andExpect(jsonPath("$.data.event.title").value(requestDto.getTitle()))
                .andExpect(jsonPath("$.data.event.selectableParticipleTimes").exists())
                .andExpect(jsonPath("$.data.event.selectedParticipleTimes").exists())
                .andExpect(jsonPath("$.data.event.color").value(requestDto.getColor().toString()))
                .andExpect(jsonPath("$.data.event.dday").exists())
        ;
    }

    @Test
    void 이벤트_삭제() throws Exception {
        // given
        final User user = UserTestFixture.createUser();
        userRepository.save(user);

        final String token = login(user);

        final Event event = EventTestFixture.createEvent(user);
        eventRepository.save(event);

        //when & then
        mockMvc.perform(
                        delete("/v1/events/{id}", event.getId())
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
        ;
    }

    @Test
    void 이벤트_게스트_로그인_DB에_없는_경우() throws Exception {
        // given
        final User user = UserTestFixture.createUser();
        userRepository.save(user);

        final Event event = EventTestFixture.createEvent(user);
        eventRepository.save(event);


        final var loginRequest = GuestLoginRequest.builder()
                .name("테스트 게스트")
                .password("1234")
                .build();

        //when & then
        mockMvc.perform(
                        post("/v1/events/{eventId}/guests/login", event.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(loginRequest.getName()))
                .andExpect(jsonPath("$.data.token").exists())
        ;
    }

    @Test
    void 이벤트_게스트_로그인() throws Exception {
        // given
        final User user = UserTestFixture.createUser();
        userRepository.save(user);

        final Event event = EventTestFixture.createEvent(user);
        eventRepository.save(event);

        final var guestCreateDto = GuestTestFixture.createGuestCreateDto(event);
        guestService.create(guestCreateDto);

        final var loginRequest = GuestLoginRequest.builder()
                .name(guestCreateDto.getName())
                .password(guestCreateDto.getPassword())
                .build();

        //when & then
        mockMvc.perform(
                        post("/v1/events/{eventId}/guests/login", event.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(guestCreateDto.getName()))
                .andExpect(jsonPath("$.data.token").exists())
        ;
    }
}
