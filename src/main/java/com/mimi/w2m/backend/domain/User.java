package com.mimi.w2m.backend.domain;

import com.mimi.w2m.backend.common.Role;
import com.mimi.w2m.backend.converter.db.RoleConverter;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;
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
@Table(name = "user")
public class User extends BaseTimeEntity {
    @Comment("Authorization 에서의 역할")
    @Convert(converter = RoleConverter.class)
    @Column(name = "role", length = 20, nullable = false, columnDefinition = "VARCHAR(20)")
    private final Role role = Role.USER;
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Comment("가입한 사용자 이름(중복O)")
    @Column(name = "name", length = 200, nullable = false)
    private String name;
    @Comment("가입한 사용자 이메일(oauth login 에 사용(중복X)")
    @Column(name = "email", length = 200, nullable = false, unique = true)
    private String email;
    @Comment("이용자 삭제일(없으면 null)")
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.deletedAt = null;
    }

    protected User() {
    }

    public User update(String name, String email) {
        this.name = name;
        this.email = email;
        return this;
    }

    public User updateName(String name) {
        this.name = name;
        return this;
    }

    public User updateEmail(String email) {
        this.email = email;
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
        User user = (User) o;
        return email != null && Objects.equals(email, user.email);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + "id = " + getId() + ", " + "createdDate = " + getCreatedDate() +
                ", " + "lastModifiedDate = " + getLastModifiedDate() + ", " + "role = " + getRole() + ", " + "name = " +
                getName() + ", " + "email = " + getEmail() + ", " + "deletedAt = " + getDeletedAt() + ")";
    }

    public User delete() {
        this.deletedAt = LocalDateTime.now();
        return this;
    }
}
