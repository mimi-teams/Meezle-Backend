package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.Participant;
import com.mimi.w2m.backend.dto.security.ParticipantSession;
import com.mimi.w2m.backend.error.EntityDuplicatedException;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.error.UnauthorizedException;
import com.mimi.w2m.backend.repository.EventRepository;
import com.mimi.w2m.backend.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * Participant 를 관리하는 서비스
 *
 * @author yeh35
 * @since 2022-11-01
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipantService {
private final ParticipantRepository participantRepository;
private final EventRepository       eventRepository;
private final HttpSession           httpSession;

/**
 * 참여자 생성
 *
 * @author yeh35
 * @since 2022-11-01
 */
@Transactional
public Participant createParticipant(Long eventId, String name, String password) throws EntityDuplicatedException,
                                                                                        EntityNotFoundException {
    participantRepository.findByName(name).ifPresent(entity -> {
        throw new EntityDuplicatedException("이미 존재하는 참여자 : " + name, "이미 존재하는 참여자");
    });
    var event = eventRepository.findById(eventId)
                               .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이벤트 : " + eventId, "존재하지 않는 " +
                                                                                                          "이벤트"));

    var salt     = generateSalt(Participant.getSaltLength());
    var hashedPw = generateHashedPw(salt, password);

    var participant = Participant.builder()
                                 .name(name)
                                 .event(event)
                                 .password(hashedPw)
                                 .salt(salt)
                                 .build();
    return participantRepository.save(participant);
}

private String generateSalt(Integer length) {
    return RandomString.make(length);
}

private String generateHashedPw(String salt, String password) {
    StringBuilder hashedPw = password == null ? null : new StringBuilder(password);
    var           pepper   = 5;
    for(int i = 0;
        i < pepper;
        i++) {
        hashedPw = (hashedPw == null ? new StringBuilder("null") : hashedPw).append(salt);
        hashedPw = new StringBuilder(String.valueOf(hashedPw.toString().hashCode()));
    }
    return hashedPw.toString();
}

public List<Participant> getAllParticipantInEvent(Long eventId) throws EntityNotFoundException {
    var event =
            eventRepository.findById(eventId)
                           .orElseThrow(() -> new EntityNotFoundException(("존재하지 않는 이벤트 : " + eventId), "존재하지 않는 이벤트"));
    return participantRepository.findAllByEvent(event);
}

/**
 * name = CK!
 *
 * @author teddy
 * @since 2022/11/19
 **/
public Participant updateParticipantName(Long participantId, String name) throws EntityNotFoundException,
                                                                                 EntityDuplicatedException {
    var participant = participantRepository.findById(participantId)
                                           .orElseThrow(() -> new EntityNotFoundException(
                                                   "존재하지 않는 참여자 : " + participantId, "존재하지 않는 참여자"));
    participantRepository.findByName(name).ifPresent((entity) -> {
        throw new EntityDuplicatedException("이미 존재하는 참여자 : " + name, "이미 존재하는 참여자");
    });
    return participant.updateName(name);
}

/**
 * password 의 경우, hashed 로 바뀔 것을 고려하여 name 과 password update 를 별도로 만든다.
 *
 * @author teddy
 * @since 2022/11/19
 **/
public Participant updateParticipantPassword(Long participantId, String password) throws EntityNotFoundException {
    var participant = participantRepository.findById(participantId)
                                           .orElseThrow(() -> new EntityNotFoundException(
                                                   "존재하지 않는 참여자 : " + participantId, "존재하지 않는 참여자"));

    var salt     = generateSalt(Participant.getSaltLength());
    var hashedPw = generateHashedPw(salt, password);
    return participant.updatePassword(hashedPw, salt);
}

/**
 * 참여자 삭제하기
 *
 * @author teddy
 * @since 2022/11/19
 **/
public void removeParticipant(Long participantId) throws EntityNotFoundException {
    var participant = participantRepository.findById(participantId)
                                           .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 참여자 : " + participantId, "존재하지 " +
                                                                                                                            "않는 참여자"));
    participantRepository.delete(participant);
}

/**
 * 참여자 로그인 처리하기. Session 에 ParticipantSession 정보를 저장한다
 *
 * @author teddy
 * @since 2022/11/19
 **/
public Participant login(String name, String password) throws EntityNotFoundException, InvalidValueException {
    var participant =
            participantRepository.findByName(name)
                                 .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 참여자 : " + name + "존재하지 않는 " +
                                                                                "참여자"));
    var storedPw   = participant.getPassword();
    var storedSalt = participant.getSalt();
    var receivedPw = generateHashedPw(storedSalt, password);
    if(storedPw.equals(receivedPw)) {
        var participantSession = new ParticipantSession(participant);
        httpSession.setAttribute("participant", participantSession);
        return participant;
    } else {
        throw new InvalidValueException("유효하지 않은 비밀번호 : " + password, "유효하지 않은 비밀번호");
    }
}

/**
 * 참여자 로그아웃 처리하기. Session 에서 ParticipantSession 정보를 제거한다
 *
 * @author teddy
 * @since 2022/11/19
 **/
public void logout() throws EntityNotFoundException {
    Optional.ofNullable((ParticipantSession) httpSession.getAttribute("participant"))
            .orElseThrow(() -> new EntityNotFoundException("로그인된 참여자 정보가 없습니다"));
    httpSession.removeAttribute("participant");
}

/**
 * 현재 참여자 가져오기
 *
 * @author teddy
 * @since 2022/11/19
 **/
public Participant getCurrentParticipant() throws UnauthorizedException {
    var participantId = -1L;
    try {
        var participantSession =
                Optional.ofNullable((ParticipantSession) httpSession.getAttribute("participant"))
                        .orElseThrow(() -> new UnauthorizedException("로그인된 참여자의 정보를 찾을 수 없습니다"));
        participantId = participantSession.getParticipantId();
        return getParticipant(participantId);
    } catch(EntityNotFoundException e) {
        throw new UnauthorizedException("정보를 찾을 수 없습니다.", "참여자 정보를 찾을 수 없습니다");
    }
}

/**
 * 참여자 가져오기
 *
 * @author yeh35
 * @since 2022-11-01
 */
public Participant getParticipant(Long participantId) throws EntityNotFoundException {
    return participantRepository.findById(participantId)
                                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 참여자 : " + participantId,
                                                                               "존재하지 않는 참여자"));
}

}
