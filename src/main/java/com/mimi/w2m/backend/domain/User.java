package com.mimi.w2m.backend.domain;

import com.mimi.w2m.backend.domain.converter.RoleConverter;
import com.mimi.w2m.backend.domain.type.Role;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Formatter;
import java.util.Objects;

/**
 * User
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/16
 **/
@Entity
@Getter
@Table(name = "mimi_user")
public class User extends BaseTimeEntity {
@Comment("Authorization 에서의 역할")
@Convert(converter = RoleConverter.class)
@Column(name = "role", length = 50, nullable = false, columnDefinition = "VARCHAR(20)")
private final Role          role = Role.USER;
@Id
@Column(name = "user_id")
@GeneratedValue(strategy = GenerationType.IDENTITY)
private       Long          id;
@Comment("가입한 사용자 이름(중복O)")
@Column(name = "name", length = 200, nullable = false)
private       String        name;
@Comment("가입한 사용자 이메일(oauth login 에 사용(중복X)")
@Column(name = "email", length = 200, nullable = false, unique = true)
private       String        email;
@Comment("이용자 삭제일(없으면 null)")
@Column(name = "deleted_at")
private       LocalDateTime deletedAt;

@Builder
public User(String name, String email) {
    this.name      = name;
    this.email     = email;
    this.deletedAt = null;
}

protected User() {
}

public User update(String name, String email) {
    this.name  = name;
    this.email = email;
    return this;
}

@Override
public String toString() {
    final var formatter = new Formatter();
    return formatter.format("UserEntity[name=%s, email=%s, deletedAt=%s]", this.name, this.email, this.deletedAt).toString();
}

@Override
public int hashCode() {
    return getClass().hashCode();
}

/**
 * 후보키인 email 을 기준으로 비교한다(test consistency 유지를 위해)
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
    User user = (User) o;
    return email != null && Objects.equals(email, user.email);
}

public User delete() {
    this.deletedAt = LocalDateTime.now();
    return this;
}
}
