package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.Event;
import com.mimi.w2m.backend.domain.Guest;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.guest.GuestRequestDto;
import com.mimi.w2m.backend.dto.security.LoginInfo;
import com.mimi.w2m.backend.error.EntityDuplicatedException;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

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
private final EventService    eventService;
private final HttpSession     httpSession;

public List<Guest> getAllInEvent(Long eventId) throws EntityNotFoundException {
    final var event = eventService.getEvent(eventId);
    return guestRepository.findAllByEvent(event);
}

@Transactional
public Guest update(Long guestId, GuestRequestDto requestDto) throws EntityNotFoundException,
                                                                     EntityDuplicatedException {
    updateName(guestId, requestDto.getName());
    return updatePassword(guestId, requestDto.getPassword());
}

/**
 * name = CK!
 *
 * @author teddy
 * @since 2022/11/19
 **/
protected Guest updateName(Long guestId, String name) throws EntityNotFoundException,
                                                             EntityDuplicatedException {
    final var guest = get(guestId);
    if(!Objects.equals(guest.getName(), name)) {
        notExist(name, guest.getEvent());
        return guest.updateName(name);
    } else {
        return guest;
    }
}

/**
 * password 의 경우, hashed 로 바뀔 것을 고려하여 name 과 password update 를 별도로 만든다.
 *
 * @author teddy
 * @since 2022/11/19
 **/
protected Guest updatePassword(Long guestId, String password) throws EntityNotFoundException {
    final var guest    = get(guestId);
    final var salt     = generateSalt(Guest.getSaltLength());
    final var hashedPw = generateHashedPw(salt, password);
    return guest.updatePassword(hashedPw, salt);
}

/**
 * 참여자 가져오기
 *
 * @author yeh35
 * @since 2022-11-01
 */
public Guest get(Long guestId) throws EntityNotFoundException {
    return guestRepository.findById(guestId)
                          .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 참여자 : " + guestId,
                                                                         "존재하지 않는 참여자"));
}

public void notExist(String name, Event event) throws EntityDuplicatedException {
    guestRepository.findByNameInEvent(name, event)
                   .ifPresent(entity -> {
                       throw new EntityDuplicatedException("이미 존재하는 참여자 : " + name,
                                                           "이미 존재하는 참여자");
                   });
}

private String generateSalt(Integer length) {
    return RandomString.make(length);
}

private String generateHashedPw(String salt, String password) {
    StringBuilder hashedPw = password == null ? null : new StringBuilder(password);
    final var     pepper   = 5;
    for(int i = 0;
        i < pepper;
        i++) {
        hashedPw = (hashedPw == null ? new StringBuilder("null") : hashedPw).append(salt);
        hashedPw = new StringBuilder(String.valueOf(hashedPw.toString().hashCode()));
    }
    return hashedPw.toString();
}

/**
 * 참여자 삭제하기
 *
 * @author teddy
 * @since 2022/11/19
 **/
@Transactional
public void remove(Long guestId) throws EntityNotFoundException {
    final var guest = get(guestId);
    guestRepository.delete(guest);
}

/**
 * 참여자 로그인 처리하기. Session 에 ParticipantSession 정보를 저장한다
 *
 * @author teddy
 * @since 2022/11/19
 **/
@Transactional
public Guest login(GuestRequestDto requestDto) throws InvalidValueException {
    final var event = eventService.getEvent(requestDto.getEventId());
    final var guest = guestRepository.findByNameInEvent(requestDto.getName(), event)
                                     .orElse(create(requestDto));
    final var storedPw   = guest.getPassword();
    final var storedSalt = guest.getSalt();
    final var receivedPw = generateHashedPw(storedSalt, requestDto.getPassword());
    if(storedPw.equals(receivedPw)) {
        final var info = new LoginInfo(guest.getId(), Role.GUEST);
        httpSession.setAttribute(LoginInfo.key, info);
        return guest;
    } else {
        throw new InvalidValueException("유효하지 않은 비밀번호 : " + requestDto.getPassword(), "유효하지 않은 비밀번호");
    }
}

/**
 * 참여자 생성
 *
 * @author yeh35
 * @since 2022-11-01
 */
@Transactional
protected Guest create(GuestRequestDto requestDto) throws EntityDuplicatedException,
                                                          EntityNotFoundException {
    final var event = eventService.getEvent(requestDto.getEventId());
    notExist(requestDto.getName(), event);
    final var salt     = generateSalt(Guest.getSaltLength());
    final var hashedPw = generateHashedPw(salt, requestDto.getPassword());
    return guestRepository.save(requestDto.to(event, salt, hashedPw));
}

@Transactional
public void deleteAll(List<Guest> guests) {
    guestRepository.deleteAll(guests);
}

@Transactional
public void delete(Guest guest) {
    guestRepository.delete(guest);
}
}
