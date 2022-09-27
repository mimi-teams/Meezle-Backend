package com.mimi.w2m.backend.domain;

import com.mimi.w2m.backend.domain.core.BaseTimeEntity;
import lombok.Generated;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 유저
 *
 * @since 2022-09-27
 * @auther yeh35
 */

@Getter
@Entity
@Table(name = "w2m_user")
public class User extends BaseTimeEntity {

    @Generated
    @Id
    private Long id;

    @Column(name = "name", length = 200)
    @Comment("이름")
    private String name;

    @Column(name = "email", length = 200)
    @Comment("이메일")
    private String email;

    protected User() { }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
