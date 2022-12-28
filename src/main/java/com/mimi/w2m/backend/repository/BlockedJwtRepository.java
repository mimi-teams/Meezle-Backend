package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.domain.BlockedJwt;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BlockedJwtRepository
 * @author teddy
 * @version 1.0.0
 * @since 2022/12/28
**/
public interface BlockedJwtRepository extends JpaRepository<BlockedJwt, String> {

}