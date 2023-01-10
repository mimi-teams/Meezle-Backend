package com.mimi.w2m.backend.client.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mimi.w2m.backend.client.kakao.api.KaKaoApiClient;
import com.mimi.w2m.backend.client.kakao.dto.calendar.KakaoCalendarGetResponse;
import com.mimi.w2m.backend.client.kakao.dto.calendar.KakaoCalendarPostResponse;
import com.mimi.w2m.backend.client.kakao.dto.calendar.event.KakaoCalendarEvent;
import com.mimi.w2m.backend.client.kakao.dto.calendar.event.KakaoCalendarEventPostResponse;
import com.mimi.w2m.backend.client.kakao.dto.calendar.type.KakaoCalendarType;
import com.mimi.w2m.backend.config.exception.InvalidValueException;
import com.mimi.w2m.backend.dto.calendar.CalendarEventPostRequest;
import com.mimi.w2m.backend.dto.calendar.CalendarPostRequest;
import com.mimi.w2m.backend.repository.EventParticipantRepository;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.repository.EventSelectableParticipleTimeRepository;
import com.mimi.w2m.backend.repository.GuestRepository;
import com.mimi.w2m.backend.service.EventService;
import com.mimi.w2m.backend.service.UserService;
import com.mimi.w2m.backend.utils.HttpUtils;
import org.springframework.stereotype.Service;

/**
 * KakaoService
 *
 * @author teddy
 * @version 1.0.0
 * @since 2023/01/09
 **/
@Service
public class KakaoService extends EventService {
    private final KaKaoApiClient kakaoApiClient;

    public KakaoService(KaKaoApiClient kakaoApiClient, UserService userService, EventRepository eventRepository, EventSelectableParticipleTimeRepository eventSelectableParticipleTimeRepository, GuestRepository guestRepository, EventParticipantRepository eventParticipantRepository) {
        super(userService, eventRepository, eventSelectableParticipleTimeRepository, guestRepository, eventParticipantRepository);
        this.kakaoApiClient = kakaoApiClient;
    }

    public KakaoCalendarGetResponse getKakaoCalendars(String accessToken, KakaoCalendarType filter) {
        return kakaoApiClient.getCalenders(HttpUtils.withBearerToken(accessToken), filter);
    }

    public KakaoCalendarPostResponse createKakaoCalendar(String accessToken, CalendarPostRequest request) {
        return kakaoApiClient
                .createCalendar(HttpUtils.withBearerToken(accessToken), request.name(), request.color(), request.reminder(), request.reminderAllDay());
    }

    public KakaoCalendarEventPostResponse createKakaoCalendarEvent(String accessToken, String calendarId, CalendarEventPostRequest request) {
        final var mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        final var kakaoEventPostRequest = KakaoCalendarEvent.of(request);
        try {
            final var kakaoRequest = mapper.writeValueAsString(kakaoEventPostRequest);
            return kakaoApiClient
                    .createCalendarEvent(HttpUtils.withBearerToken(accessToken), calendarId, kakaoRequest);
        } catch (JsonProcessingException e) {
            throw new InvalidValueException(e.getMessage());
        }
    }
}