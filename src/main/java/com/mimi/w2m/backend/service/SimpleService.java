package com.mimi.w2m.backend.service;

import com.mimi.w2m.backend.repository.SimpleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 예제용 Service
 *
 * @since 2022-09-27
 * @auther yeh35
 */

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SimpleService {

    private final SimpleRepository simpleRepository;

    @Transactional
    public void sdfadfa() {
        simpleRepository.save();
        simpleRepository.save();
    }

}
