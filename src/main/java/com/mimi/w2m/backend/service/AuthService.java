package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.security.SessionInfo;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Formatter;
import java.util.Objects;

/**
 * AuthService(Authorization 확인)
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/25
 **/
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
private final HttpSession        httpSession;
private final UserService        userService;
private final EventService       eventService;
private final ParticipantService participantService;

void isCurrentLogin(Long id, Role role) throws UnauthorizedException, EntityNotFoundException {
    final var info = (SessionInfo) httpSession.getAttribute(SessionInfo.key);
    if(Objects.isNull(info) || !Objects.equals(info.loginId(), id) || !Objects.equals(info.role(), role)) {
        final var formatter = new Formatter();
        final var msg = Objects.isNull(info) ?
                        formatter.format("로그인된 이용자 정보와 불일치 : received[id=%d, role=%s] <-> stored[null]",
                                         id, role).toString() :
                        formatter.format("로그인된 이용자 정보와 불일치 : received[id=%d, role=%s] <-> stored[id=%d, role=%s]",
                                         id, role, info.loginId(), info.role()).toString();
        throw new UnauthorizedException(msg, "로그인된 이용자 정보와 불일치");
    } else {
        //Check EntityNotFound
        switch(role) {
            case USER -> userService.getUser(id);
            case PARTICIPANT -> participantService.getParticipant(id);
            case NONE -> throw new UnauthorizedException("유효하지 않은 사용자 : " + role, "유효하지 않은 사용자");
        }
    }
}

void isHost(Long userId, Long eventId) throws UnauthorizedException, EntityNotFoundException {
    final var event = eventService.getEvent(eventId);
    final var user  = userService.getUser(userId);
    if(!event.getUser().equals(user)) {
        throw new UnauthorizedException("유효하지 않은 호스트 : userId=" + userId + ", eventId=" + eventId, "유효하지 않은 호스트");
    }
}
}