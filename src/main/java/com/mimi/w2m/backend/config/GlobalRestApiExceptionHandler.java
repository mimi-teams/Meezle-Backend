package com.mimi.w2m.backend.config;

import com.mimi.w2m.backend.config.exception.*;
import com.mimi.w2m.backend.config.exception.IllegalAccessException;
import com.mimi.w2m.backend.dto.base.ApiCallResponse;
import com.mimi.w2m.backend.dto.base.ApiResultCode;
import org.junit.jupiter.api.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Formatter;

/**
 * Exception Handler 요청 시 발생 가능한 예외 사항에 대한 반환 값을 다룬다
 *
 * @author teddy, yeh35
 * @since 2022.12.04
 **/
@RestControllerAdvice
public class GlobalRestApiExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalRestApiExceptionHandler.class.getName());
    private final Formatter formatter = new Formatter();

    /**
     * 요청된 정보가 중복된 것일 때
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(EntityDuplicatedException.class)
    public ResponseEntity<ApiCallResponse<?>> handleEntityDuplicatedException(EntityDuplicatedException e) {
        final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
        logger.warn(log);

        final var response = ApiCallResponse.of(ApiResultCode.ENTITY_DUPLICATED, e.messageToClient, null);
        return new ResponseEntity<>(response, e.apiResultCode.httpStatus.toHttpStatus());
    }

    /**
     * 요청된 정보가 없을 때
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiCallResponse<?>> handleEntityNotFoundException(EntityNotFoundException e) {
        final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
        logger.warn(log);

        final var response = ApiCallResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
        return new ResponseEntity<>(response, e.apiResultCode.httpStatus.toHttpStatus());
    }

    /**
     * 잘못된 값으로 요청할 때
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<ApiCallResponse<?>> handleInvalidValueException(InvalidValueException e) {
        final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
        logger.warn(log);

        final var response = ApiCallResponse.of(ApiResultCode.BAD_REQUEST, e.messageToClient, null);
        return new ResponseEntity<>(response, e.apiResultCode.httpStatus.toHttpStatus());
    }

    /**
     * 권한 없는 이용자가 요청할 때
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ApiCallResponse<?>> handleUnauthorizedException(IllegalAccessException e) {
        final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
        logger.warn(log);

        final var response = ApiCallResponse.of(ApiResultCode.ILLEGAL_ACCESS, e.messageToClient, null);
        return new ResponseEntity<>(response, e.apiResultCode.httpStatus.toHttpStatus());
    }

    /**
     * Validation 이 실패할 때
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiCallResponse<?>> handleMethodArgumentNotValid(ConstraintViolationException e) {
        logger.warn("400 Exception occurs. " + e.getMessage());

        final var response = ApiCallResponse.of(ApiResultCode.BAD_REQUEST, e.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * 서버 내부의 문제일 때
     */
    @Order(Ordered.LOWEST_PRECEDENCE)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiCallResponse<?>> handleBaseException(BusinessException e) {
        logger.error("Unexpected Exception occurs", e);
        final var response = ApiCallResponse.of(ApiResultCode.SERVER_ERROR, e.message, null);
        return new ResponseEntity<>(response, e.apiResultCode.httpStatus.toHttpStatus());
    }
}