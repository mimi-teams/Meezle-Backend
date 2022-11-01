package com.mimi.w2m.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User Domain의 관리를 책임지는 서비스
 *
 * @since 2022-11-01
 * @author paul
 */

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {


    /**
     * 회원가입
     *
     * @since 2022-11-01
     * @author paul
     */
    public void signup() {
        
    }
    
    
    /**
     * 유저 정보 가져오기
     *
     * @since 2022-11-01
     * @author paul
     */
    public void getUser(Long userId) {

    }

}
