package com.mimi.w2m.backend.domain;

import com.mimi.w2m.backend.domain.converter.RoleConverter;
import com.mimi.w2m.backend.domain.type.Role;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.Formatter;
import java.util.Objects;

/**
 * Guest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/16
 **/
@Entity
@Getter
@Table(name = "meezle_guest", uniqueConstraints = {@UniqueConstraint(columnNames = {"name",
                                                                                    "event_id"})})
public class Guest extends BaseTimeEntity {
@Comment("Authorization 에서의 역할")
@Convert(converter = RoleConverter.class)
@Column(name = "role", length = 50, nullable = false, columnDefinition = "VARCHAR(20)")
private final Role   role = Role.GUEST;
@Id
@Column(name = "guest_id")
@GeneratedValue(strategy = GenerationType.IDENTITY)
private       Long   id;
@Comment("참여자의 이름")
@Column(name = "name", length = 200, nullable = false)
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
public Guest(String name, String password, String salt, Event event) {
    this.name     = name;
    this.password = password;
    this.salt     = salt;
    this.event    = event;
}

protected Guest() {
}

public static Integer getSaltLength() {
    return 200;
}

public Guest updateName(String name) {
    this.name = name;
    return this;
}

public Guest updatePassword(String password, String salt) {
    this.password = password;
    this.salt     = salt;
    return this;
}

@Override
public String toString() {
    final var formatter = new Formatter();
    return formatter
                   .format("GuestEntity[name=%s, password=%s, slat=%s]", this.name, this.password, this.salt)
                   .toString();
}

@Override
public int hashCode() {
    return getClass().hashCode();
}

/**
 * 후보키인 name 을 기준으로 비교한다
 *
 * @author teddy
 * @since 2022/11/25
 **/
@Override
public boolean equals(Object o) {
    if(this == o) {
        return true;
    }
    if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
        return false;
    }
    Guest that = (Guest) o;
    return name != null && Objects.equals(name, that.name);
}
}