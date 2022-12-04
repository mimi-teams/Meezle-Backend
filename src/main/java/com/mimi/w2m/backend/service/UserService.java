package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.repository.UserRepository;
import com.mimi.w2m.backend.type.domain.User;
import com.mimi.w2m.backend.type.dto.user.UserRequestDto;
import com.mimi.w2m.backend.type.response.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Formatter;
import java.util.Optional;

/**
 * UserService
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/19
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final HttpSession httpSession;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class.getName());

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 이용자 정보를 저장한다
     *
     * @author paul
     * @since 2022-11-01
     */
    @Transactional
    protected User signup(String name, String email) {
        return userRepository.save(User.builder()
                .name(name)
                .email(email)
                .build());
    }

    /**
     * 가입된 이용자 정보 삭제
     *
     * @author teddy
     * @since 2022/11/19
     **/
    @Transactional
    public void deleteReal(Long userId) throws EntityNotFoundException {
        var user = get(userId);
        userRepository.delete(user);
    }

    /**
     * 가입된 이용자 정보 가져오기
     *
     * @author paul
     * @since 2022-11-01
     */
    public User get(Long id) throws EntityNotFoundException {
        final var user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            final var formatter = new Formatter();
            final var msg = formatter.format("[UserService] Entity Not Found" + "(id=%d)", id)
                    .toString();
            throw new EntityNotFoundException(msg);
        }
    }

    /**
     * 이용자 삭제하기(deletedAt 만 설정)
     *
     * @author teddy
     * @since 2022/11/27
     **/
    @Transactional
    public void deleteNotReal(Long userId) throws EntityNotFoundException {
        var user = get(userId);
        user.delete();
    }

    public User getByEmail(String email) throws EntityNotFoundException {
        final var user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            final var formatter = new Formatter();
            final var msg = formatter.format("[UserService] Entity Not Found" + "(email=%s)", email)
                    .toString();
            throw new EntityNotFoundException(msg);
        }
    }
    // TODO: 2022/12/01 Email 을 변경 가능하도록 수정하기

    /**
     * 이용자 정보 변경하기. 현재 Email 을 변경할 수는 없다
     *
     * @author teddy
     * @since 2022/11/19
     **/
    @Transactional
    public User update(Long userId, UserRequestDto requestDto) throws EntityNotFoundException {
        final var user = get(userId);
        return user.updateName(requestDto.getName());
    }
}