package com.mimi.w2m.backend.domain;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

/**
 * Guest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/16
 **/
@Entity
@Getter
@Table(name = "guest", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "event_id"})})
public class Guest extends BaseTimeEntity {

    @Id
    @GenericGenerator(name="sequential_uuid", strategy="com.mimi.w2m.backend.domain.generator.SequentialUUIDGenerator")
    @GeneratedValue(generator="sequential_uuid")
    @Column(name = "guest_id", columnDefinition = "BINARY(16)")
    private UUID id;
    @Comment("참여자의 이름")
    @Column(name = "name", length = 20, nullable = false, columnDefinition = "VARCHAR(20)")
    private String name;

    @Comment("참여자 비밀번호(없어도 가능!)")
    @Column(name = "password")
    private String password;

    @Comment("연관된 event")
    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", updatable = false, nullable = false)
    private Event event;

    @Builder
    public Guest(String name, String password, Event event) {
        this.name = name;
        this.password = password;
        this.event = event;
    }

    protected Guest() {
    }

    public static Integer getSaltLength() {
        return 200;
    }

    public Guest update(String name, String password, String salt) {
        updateName(name);
        return updatePassword(password);
    }

    public Guest updateName(String name) {
        this.name = name;
        return this;
    }

    public Guest updatePassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Guest guest = (Guest) o;
        return Objects.equals(event, guest.event) && (name != null && Objects.equals(name, guest.name));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + "id = " + getId() + ", " + "createdDate = " + getCreatedDate() +
                ", " + "lastModifiedDate = " + getLastModifiedDate() + ", " + "name = " +
                getName() + ", " + "password = " + getPassword() + ")";
    }
}