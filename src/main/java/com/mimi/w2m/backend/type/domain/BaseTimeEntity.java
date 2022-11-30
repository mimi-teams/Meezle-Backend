package com.mimi.w2m.backend.type.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 최상위 Entity로 생성시간과 최종 수정 시간 정보를 가지고 있다.
 *
 * @since 2022-09-27
 */
@Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

@CreatedDate
@Column(updatable = false, nullable = false)
private LocalDateTime createdDate = LocalDateTime.of(1000, 1, 1, 0, 0, 0);

@LastModifiedDate
@Column(nullable = false)
private LocalDateTime lastModifiedDate = LocalDateTime.of(1000, 1, 1, 0, 0, 0);

}
