package com.mimi.w2m.backend.domain;

import com.mimi.w2m.backend.domain.converter.RoleConverter;
import com.mimi.w2m.backend.domain.type.Role;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.Formatter;

/**
 * Participant
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/16
 **/
@Entity
@Getter
@Table(name = "meezle_participant")
public class Participant extends BaseTimeEntity {
@Comment("Aothorization에서의 역할")
@Convert(converter = RoleConverter.class)
@Column(name = "role", length = 50, nullable = false, columnDefinition = "VARCHAR(20)")
private final Role   role = Role.PARTICIPANT;
@Id
@Column(name = "participant_id")
@GeneratedValue(strategy = GenerationType.IDENTITY)
private       Long   id;
@Comment("참여자의 이름")
@Column(name = "name", length = 200, nullable = false, unique = true)
private       String name;
@Comment("Salt")
@Column(name = "salt", length = 200)
private       String salt;
@Comment("참여자 비밀번호(없어도 가능!)")
@Column(name = "password", length = 200)
private       String password;
@Comment("연관된 event")
@ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "event_id", updatable = false)
private       Event  event;

@Builder
public Participant(String name, String password, String salt, Event event) {
    this.name     = name;
    this.password = password;
    this.salt     = salt;
    this.event    = event;
}

protected Participant() {
}

public static Integer getSaltLength() {
    return 200;
}

public Participant updateName(String name) {
    this.name = name;
    return this;
}

public Participant updatePassword(String password, String salt) {
    this.password = password;
    this.salt     = salt;
    return this;
}

@Override
public String toString() {
    final var formatter = new Formatter();
    return formatter
                   .format("ParticipantEntity[name=%s, password=%s, slat=%s]", this.name, this.password, this.salt)
                   .toString();
}
}