package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.config.exception.EntityNotFoundException;
import com.mimi.w2m.backend.config.exception.InvalidValueException;
import com.mimi.w2m.backend.config.interceptor.JwtHandler;
import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.Guest;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.guest.GuestCreateDto;
import com.mimi.w2m.backend.dto.guest.GuestLoginRequest;
import com.mimi.w2m.backend.dto.guest.GuestLoginResponse;
import com.mimi.w2m.backend.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Formatter;
import java.util.Optional;

/**
 * Guest 를 관리하는 서비스
 *
 * @author yeh35
 * @since 2022-11-01
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuestService {
    private final GuestRepository guestRepository;
    private final EventService eventService;

    private final JwtHandler jwtHandler;

    private final PasswordEncoder passwordEncoder;


    /**
     * 참여자 가져오기
     *
     * @author yeh35
     * @since 2022-11-01
     */
    public Guest getGuest(Long id) throws EntityNotFoundException {
        final var guest = guestRepository.findById(id);
        if (guest.isPresent()) {
            return guest.get();
        } else {
            final var formatter = new Formatter();
            final var msg = formatter.format("[GuestService] Entity Not Found(id=%d)", id)
                    .toString();
            throw new EntityNotFoundException(msg);
        }
    }

    /**
     * 사용중인 유저 이름인지
     * @return 사용중이라면 true, 아니라면 false
     */
    public boolean isUsedGuestName(Long eventId, String guestName) {
        final Event event = eventService.getEvent(eventId);
        Optional<Guest> byNameAndEvent = guestRepository.findByNameAndEvent(guestName, event);
        return byNameAndEvent.isPresent();
    }

    /**
     * 게스트 로그인
     *
     * @author yeh35
     * @since 2022-12-17
     */
    public GuestLoginResponse login(Long eventId, GuestLoginRequest loginDto) {
        final Event event = eventService.getEvent(eventId);

        Optional<Guest> byNameAndEvent = guestRepository.findByNameAndEvent(loginDto.getName(), event);
        if (byNameAndEvent.isEmpty()) {
            throw new InvalidValueException("존재하지 않는 게스트입니다.");
        }
        final Guest guest = byNameAndEvent.get();

        if (!passwordEncoder.matches(loginDto.getPassword(), guest.getPassword())) {
            throw new InvalidValueException("올바르지 비밀번호입니다.");
        }

        final String token = jwtHandler.createToken(guest.getId(), Role.GUEST);

        return new GuestLoginResponse(guest.getName(), token);
    }

    /**
     * 게스트 생성
     *
     * @author yeh35
     * @since 2022-12-17
     */
    @Transactional
    public Guest create(GuestCreateDto createDto) {
        final Event event = eventService.getEvent(createDto.getEventId());

        // 비밀번호 헤슁
        final String password = passwordEncoder.encode(createDto.getPassword());

        final var guest = Guest.builder()
                .name(createDto.getName())
                .password(password)
                .event(event)
                .build();
        guestRepository.save(guest);

        return guest;
    }
}
