package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.repository.EventParticipantRepository;
import com.mimi.w2m.backend.repository.GuestRepository;
import com.mimi.w2m.backend.type.common.Role;
import com.mimi.w2m.backend.type.domain.EventParticipant;
import com.mimi.w2m.backend.type.domain.Guest;
import com.mimi.w2m.backend.type.dto.guest.GuestRequestDto;
import com.mimi.w2m.backend.type.dto.security.LoginInfo;
import com.mimi.w2m.backend.type.response.exception.EntityDuplicatedException;
import com.mimi.w2m.backend.type.response.exception.EntityNotFoundException;
import com.mimi.w2m.backend.type.response.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Formatter;
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
    private final EventParticipantRepository eventParticipantRepository;
    private final EventService eventService;
    private final HttpSession httpSession;

    public List<Guest> getAllInEvent(Long eventId) throws EntityNotFoundException {
        final var event = eventService.get(eventId);
        final var guests = guestRepository.findAllByEvent(event);
        if (guests.isEmpty()) {
            final var formatter = new Formatter();
            final var msg = formatter.format("[GuestService] Entity Not Found(event=%d)", eventId)
                    .toString();
            throw new EntityNotFoundException(msg);
        } else {
            return guests;
        }
    }

    /**
     * Guest 정보 수정하기
     *
     * @author teddy
     * @since 2022/12/01
     **/
    @Transactional
    public Guest update(Long id, GuestRequestDto requestDto) throws EntityNotFoundException, EntityDuplicatedException {
        final var guest = get(id);
        updateName(guest, requestDto.getName(), requestDto.getEventId());
        return updatePassword(guest, requestDto.getPassword());
    }

    /**
     * 참여자 가져오기
     *
     * @author yeh35
     * @since 2022-11-01
     */
    public Guest get(Long id) throws EntityNotFoundException {
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
     * update() 에 엮여있는 method 이므로 동일한 Transaction 이어야 한다
     *
     * @author teddy
     * @since 2022/11/19
     **/
    protected Guest updateName(Guest guest, String name, Long eventId)
            throws EntityNotFoundException, EntityDuplicatedException {
        final var event = eventService.get(eventId);
        if (Objects.equals(guest.getName(), name)) {
            return guest;
        } else {
            final var other = guestRepository.findByNameInEvent(name, event);
            if (other.isEmpty()) {
                return guest.updateName(name);
            } else {
                final var formatter = new Formatter();
                final var msg = formatter.format("[GuestService] Entity Duplicated(name=%s, event=%d)", name, eventId)
                        .toString();
                throw new EntityDuplicatedException(msg);
            }
        }
    }

    /**
     * Salt 와 HashedPw 를 만들어 저장한다
     *
     * @author teddy
     * @since 2022/11/19
     **/
    protected Guest updatePassword(Guest guest, String password) {
        final var salt = generateSalt(Guest.getSaltLength());
        final var hashedPw = generateHashedPw(salt, password);
        return guest.updatePassword(hashedPw, salt);
    }

    private String generateSalt(Integer length) {
        return RandomString.make(length);
    }

    private String generateHashedPw(String salt, String password) {
        StringBuilder hashedPw = password == null ? null : new StringBuilder(password);
        final var pepper = 5;
        for (int i = 0; i < pepper; i++) {
            hashedPw = (hashedPw == null ? new StringBuilder("null") : hashedPw).append(salt);
            hashedPw = new StringBuilder(String.valueOf(hashedPw.toString()
                    .hashCode()));
        }
        return hashedPw.toString();
    }

    /**
     * 참여자 로그인 처리하기(참여자 정보가 없다면, 새롭게 생성한다). Session 에 ParticipantSession 정보를 저장한다
     *
     * @author teddy
     * @since 2022/11/19
     **/
    @Transactional
    public Guest login(GuestRequestDto requestDto)
            throws EntityDuplicatedException, EntityNotFoundException, InvalidValueException {
        final var event = eventService.get(requestDto.getEventId());
        final var guest = guestRepository.findByNameInEvent(requestDto.getName(), event)
                .orElse(create(requestDto));
        final var storedPw = guest.getPassword();
        final var storedSalt = guest.getSalt();
        final var receivedPw = generateHashedPw(storedSalt, requestDto.getPassword());
        if (storedPw.equals(receivedPw)) {
            final var info = new LoginInfo(guest.getId(), Role.GUEST);
            httpSession.setAttribute(LoginInfo.key, info);
            return guest;
        } else {
            final var formatter = new Formatter();
            final var msg = formatter.format("[GuestService] Invalid Password(salt=%s, stored=%s, received(origin)" +
                                    "=%s, received(hashed)=%s)", storedSalt, storedPw,
                            requestDto.getPassword(), receivedPw)
                    .toString();
            throw new InvalidValueException(msg);
        }
    }

    /**
     * 참여자 생성(Participant 에 참여자를 추가한다)
     *
     * @author yeh35
     * @since 2022-11-01
     */
    @Transactional
    protected Guest create(GuestRequestDto requestDto) throws EntityDuplicatedException, EntityNotFoundException {
        final var event = eventService.get(requestDto.getEventId());
        final var other = guestRepository.findByNameInEvent(requestDto.getName(), event);
        if (other.isPresent()) {
            final var formatter = new Formatter();
            final var msg = formatter.format("[GuestService] Entity Duplicated(event=%d, name=%s)", event.getId(),
                            requestDto.getName())
                    .toString();
            throw new EntityDuplicatedException(msg);
        } else {
            final var salt = generateSalt(Guest.getSaltLength());
            final var hashedPw = generateHashedPw(salt, requestDto.getPassword());
            final var guest = guestRepository.save(requestDto.to(event, salt, hashedPw));

            final var participant = EventParticipant.builder()
                    .event(event)
                    .guest(guest)
                    .build();
            eventParticipantRepository.save(participant);
            return guest;
        }
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
