package com.mimi.w2m.backend.config;


import com.mimi.w2m.backend.dto.base.ApiCallResponse;
import com.mimi.w2m.backend.dto.base.ApiResultCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

/**
 *
 * `ResponseEntityExceptionHandler`를 사용하려면 `@RestControllerAdvice`가 아니라 `@ControllerAdvice`여야한다.
 *
 * @since 2022-12-25
 * @author yeh35
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 요청한 URI 가 없을 때
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        logger.warn("404 Exception occurs. URI=" + ex.getRequestURL());
        return ResponseEntity.of(Optional.of(ApiCallResponse.of(ApiResultCode.BAD_REQUEST, null)));
    }

}
