package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.BlockedJwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

/**
 * BlockedJwtRepository
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/12/28
 **/
public interface BlockedJwtRepository extends JpaRepository<BlockedJwt, String> {
    @Modifying
    void deleteByCreatedDateBefore(@Param("threshold") LocalDateTime threshold);

}