package com.mimi.w2m.backend.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BlockedJwt : 로그아웃된 Jwt Token 을 Blacklisting
 * @author teddy
 * @version 1.0.0
 * @since 2022/12/28
**/
@Entity
@Getter
@NoArgsConstructor
@Table(name = "blocked_jwt")
public class BlockedJwt extends BaseTimeEntity {
    @Id
    @Column(name = "token", unique = true, nullable = false, updatable = false)
    String token;

    public BlockedJwt(String token) {
        this.token = token;
    }
}