package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.config.constants.AttributeConstants;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.auth.CurrentUserInfo;
import com.mimi.w2m.backend.dto.security.LoginInfo;
import com.mimi.w2m.backend.config.exception.EntityNotFoundException;
import com.mimi.w2m.backend.config.exception.IllegalAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    private final UserService userService;
    private final EventService eventService;
    private final EventParticipantService eventParticipantService;

    private final HttpServletRequest request;

    /**
     * 적절한 이용자의 로그인인지 확인한다
     *
     * @author teddy
     * @since 2022/12/01
     **/
    public void isValidLogin(LoginInfo info, Long id, Role role) throws IllegalAccessException {
        if (Objects.isNull(info) || !Objects.equals(info.loginId(), id) || !Objects.equals(info.role(), role)) {
            final var formatter = new Formatter();
            final var msg = formatter.format("[AuthService] Illegal Access(id=%d, role=%s)", id, role)
                    .toString();
            throw new IllegalAccessException(msg);
        }
    }

    public void isInEvent(LoginInfo info, Long eventId) throws IllegalAccessException {
        try {
            eventParticipantService.get(eventId, info.loginId(), info.role());
        } catch (RuntimeException e) {
            final var formatter = new Formatter();
            final var msg = formatter.format("[AuthService] Illegal Access(id=%d, role=%s, event=%d)", info.loginId(),
                            info.role(), eventId)
                    .toString();
            throw new IllegalAccessException(msg);
        }
    }

    public void isHost(LoginInfo info, Long eventId) throws IllegalAccessException {
        try {
            final var event = eventService.get(eventId);
            if (Objects.equals(info.role(), Role.GUEST)) {
                throw new RuntimeException();
            }
            final var user = userService.get(info.loginId());
            if (!Objects.equals(event.getHost(), user)) {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            final var formatter = new Formatter();
            final var msg = formatter.format("[AuthService] Illegal Access(id=%d, role=%s, event=%d)", info.loginId(),
                            info.role(), eventId)
                    .toString();
            throw new IllegalAccessException(msg);
        }
    }

    public LoginInfo getLoginInfo(HttpSession httpSession) throws EntityNotFoundException {
        final var info = (LoginInfo) httpSession.getAttribute(LoginInfo.key);
        if (Objects.isNull(info)) {
            throw new EntityNotFoundException("[AuthService] There are no login users", "로그인한 이용자가 없습니다");
        }
        return info;
    }

    /**
     * 현재 유저 정보 반환
     *
     * @author yeh35
     * @since 2022-12-14
     */
    public CurrentUserInfo getCurrentUserInfo() {
        Object attribute = request.getAttribute(AttributeConstants.CURRENT_USER);

        if (attribute == null) {
            throw new EntityNotFoundException("[AuthService] There are no login users", "로그인한 이용자가 없습니다");
        }
        return (CurrentUserInfo) attribute;
    }

    public void logout(HttpSession httpSession) {
        httpSession.removeAttribute(LoginInfo.key);
    }
}