package com.mimi.w2m.backend.client.kakao.service;

import com.mimi.w2m.backend.client.kakao.api.KaKaoApiClient;
import com.mimi.w2m.backend.client.kakao.dto.calendar.KakaoCalenderListResponse;
import com.mimi.w2m.backend.client.kakao.dto.calendar.type.KakaoCalendarColor;
import com.mimi.w2m.backend.client.kakao.dto.calendar.type.KakaoCalendarType;
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

    public KakaoCalenderListResponse getKakaoCalendars(String accessToken, KakaoCalendarType filter) {
        return kakaoApiClient.getCalenders(HttpUtils.withBearerToken(accessToken), filter);
    }

    public String createKakaoCalendar(String accessToken, String name, KakaoCalendarColor color, Integer reminder, Integer reminderAllDay) {
        final var calendar = kakaoApiClient.createCalendar(HttpUtils.withBearerToken(accessToken), name, color, reminder, reminderAllDay);
        return calendar.id();
    }
}