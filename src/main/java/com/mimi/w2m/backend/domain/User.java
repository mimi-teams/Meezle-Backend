package com.mimi.w2m.backend.domain;

import com.mimi.w2m.backend.domain.converter.RoleConverter;
import com.mimi.w2m.backend.domain.type.Role;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.Formatter;

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
@Comment("Aothorization에서의 역할")
@Convert(converter = RoleConverter.class)
@Column(name = "role", length = 50, nullable = false, columnDefinition = "VARCHAR(20)")
private final Role   role = Role.USER;
@Id
@Column(name = "user_id")
@GeneratedValue(strategy = GenerationType.IDENTITY)
private       Long   id;
@Comment("가입한 사용자 이름(중복되어도 상관없다)")
@Column(name = "name", length = 200, nullable = false)
private       String name;
@Comment("가입한 사용자 이메일(oauth login 등에 사용된다). CK로 동작한다(Unique!)")
@Column(name = "email", length = 200, nullable = false, unique = true)
private       String email;

@Builder
public User(String name, String email) {
    this.name  = name;
    this.email = email;
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
    return formatter.format("UserEntity[name=%s, email=%s]", this.name, this.email).toString();
}
}
