package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.security.LoginInfo;
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
private final UserService        userService;
private final EventService       eventService;
private final ParticipantService participantService;

public void isValidLogin(Long id, Role role, HttpSession httpSession) throws UnauthorizedException,
                                                                             EntityNotFoundException {
    final var info = (LoginInfo) httpSession.getAttribute(LoginInfo.key);
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

public void isInEvent(LoginInfo info, Long eventId) {
    switch(info.role()) {
        case USER -> isHost(info, eventId);
        case PARTICIPANT -> {
            final var event            = eventService.getEvent(eventId);
            final var participantEvent = participantService.getParticipant(info.loginId()).getEvent();
            if(!Objects.equals(event, participantEvent)) {
                throw new UnauthorizedException("유효하지 않은 요청: Participant=" + info.loginId(), "이벤트 참여자가 아닙니다");
            }
        }
        case NONE -> throw new UnauthorizedException("유효하지 않은 요청: Role=" + info.role(), "유효하지 않은 이용자");
    }
}

public void isHost(LoginInfo loginInfo, Long eventId) throws UnauthorizedException, EntityNotFoundException {
    if(!Objects.equals(loginInfo.role(), Role.USER)) {
        throw new UnauthorizedException("유효하지 않은 요청: Role=" + loginInfo.role(), "이벤트 생성자가 아닙니다");
    }
    final var event = eventService.getEvent(eventId);
    final var user  = userService.getUser(loginInfo.loginId());
    if(!event.getUser().equals(user)) {
        throw new UnauthorizedException("유효하지 않은 호스트 : userId=" + loginInfo.loginId() + ", eventId=" + eventId, "유효하지" +
                                                                                                                " 않은 " +
                                                                                                                "호스트");
    }
}

public LoginInfo getCurrentLogin(HttpSession httpSession) throws EntityNotFoundException, UnauthorizedException {
    final var info = (LoginInfo) httpSession.getAttribute(LoginInfo.key);
    if(Objects.isNull(info)) {
        throw new EntityNotFoundException("로그인한 이용자가 없습니다", "로그인한 이용자가 없습니다");
    }
    switch(info.role()) {
        case USER -> userService.getUser(info.loginId());
        case PARTICIPANT -> participantService.getParticipant(info.loginId());
        case NONE -> throw new UnauthorizedException("유효하지 않은 사용자 : " + info.role(), "유효하지 않은 사용자");
    }
    return info;
}

public void logout(HttpSession httpSession) {
    httpSession.removeAttribute(LoginInfo.key);
}
}