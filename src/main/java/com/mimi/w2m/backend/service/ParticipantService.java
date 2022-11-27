package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.domain.Participant;
import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.participant.ParticipantRequestDto;
import com.mimi.w2m.backend.dto.security.SessionInfo;
import com.mimi.w2m.backend.error.EntityDuplicatedException;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.InvalidValueException;
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
public Participant createParticipant(ParticipantRequestDto requestDto) throws EntityDuplicatedException,
                                                                              EntityNotFoundException {
    participantRepository.findByName(requestDto.getName()).ifPresent(entity -> {
        throw new EntityDuplicatedException("이미 존재하는 참여자 : " + requestDto.getName(), "이미 존재하는 참여자");
    });
    var event = eventRepository.findById(requestDto.getEventId())
                               .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이벤트 : " + requestDto.getEventId(), "존재하지 않는 " +
                                                                                                                          "이벤트"));
    var salt     = generateSalt(Participant.getSaltLength());
    var hashedPw = generateHashedPw(salt, requestDto.getPassword());
    return participantRepository.save(requestDto.to(event, salt, hashedPw));
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
@Transactional
public Participant updateParticipantName(Long participantId, String name) throws EntityNotFoundException,
                                                                                 EntityDuplicatedException {
    var participant = getParticipant(participantId);
    participantRepository.findByName(name).ifPresent((entity) -> {
        throw new EntityDuplicatedException("이미 존재하는 참여자 : " + name, "이미 존재하는 참여자");
    });
    return participant.updateName(name);
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

/**
 * password 의 경우, hashed 로 바뀔 것을 고려하여 name 과 password update 를 별도로 만든다.
 *
 * @author teddy
 * @since 2022/11/19
 **/
@Transactional
public Participant updateParticipantPassword(Long participantId, String password) throws EntityNotFoundException {
    var participant = getParticipant(participantId);
    var salt        = generateSalt(Participant.getSaltLength());
    var hashedPw    = generateHashedPw(salt, password);
    return participant.updatePassword(hashedPw, salt);
}

/**
 * 참여자 삭제하기
 *
 * @author teddy
 * @since 2022/11/19
 **/
@Transactional
public void removeParticipant(Long participantId) throws EntityNotFoundException {
    var participant = getParticipant(participantId);
    participantRepository.delete(participant);
}

/**
 * 참여자 로그인 처리하기. Session 에 ParticipantSession 정보를 저장한다
 *
 * @author teddy
 * @since 2022/11/19
 **/
@Transactional
public Participant login(ParticipantRequestDto requestDto) throws EntityNotFoundException, InvalidValueException {
    var participant =
            participantRepository.findByName(requestDto.getName())
                                 .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 참여자 : " + requestDto.getName() + "존재하지 않는 " +
                                                                                "참여자"));
    var storedPw   = participant.getPassword();
    var storedSalt = participant.getSalt();
    var receivedPw = generateHashedPw(storedSalt, requestDto.getPassword());
    if(storedPw.equals(receivedPw)) {
        var info = new SessionInfo(participant.getId(), Role.PARTICIPANT);
        httpSession.setAttribute(SessionInfo.key, info);
        return participant;
    } else {
        throw new InvalidValueException("유효하지 않은 비밀번호 : " + requestDto.getPassword(), "유효하지 않은 비밀번호");
    }
}

/**
 * 참여자 로그아웃 처리하기. Session 에서 ParticipantSession 정보를 제거한다
 *
 * @author teddy
 * @since 2022/11/19
 **/
@Transactional
public void logout() throws EntityNotFoundException {
    Optional.ofNullable((SessionInfo) httpSession.getAttribute(SessionInfo.key))
            .orElseThrow(() -> new EntityNotFoundException("로그인된 참여자 정보가 없습니다"));
    httpSession.removeAttribute(SessionInfo.key);
}

public void deleteAll(List<Participant> participants) {
    participantRepository.deleteAll(participants);
}
}
