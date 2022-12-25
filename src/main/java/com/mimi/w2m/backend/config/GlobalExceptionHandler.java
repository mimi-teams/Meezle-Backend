package com.mimi.w2m.backend.config;

import com.mimi.w2m.backend.dto.base.ApiCallResponse;
import com.mimi.w2m.backend.dto.base.ApiResultCode;
import com.mimi.w2m.backend.config.exception.EntityDuplicatedException;
import com.mimi.w2m.backend.config.exception.EntityNotFoundException;
import com.mimi.w2m.backend.config.exception.IllegalAccessException;
import com.mimi.w2m.backend.config.exception.InvalidValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Formatter;
import java.util.Optional;

/**
 * Exception Handler 요청 시 발생 가능한 예외 사항에 대한 반환 값을 다룬다
 *
 * @author teddy, yeh35
 * @since 2022.12.04
 **/
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());
    private final Formatter formatter = new Formatter();

    /**
     * 요청된 정보가 중복된 것일 때
     */
    @ExceptionHandler({EntityDuplicatedException.class})
    protected ResponseEntity<ApiCallResponse<?>> handleEntityDuplicatedException(EntityDuplicatedException e) {
        final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
        logger.warn(log);

        final var response = ApiCallResponse.of(ApiResultCode.ENTITY_DUPLICATED, e.messageToClient, null);
        return new ResponseEntity<>(response, e.apiResultCode.httpStatus.toHttpStatus());
    }

    /**
     * 요청된 정보가 없을 때
     */
    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<ApiCallResponse<?>> handleEntityNotFoundException(EntityNotFoundException e) {
        final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
        logger.warn(log);

        final var response = ApiCallResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
        return new ResponseEntity<>(response, e.apiResultCode.httpStatus.toHttpStatus());
    }

    /**
     * 잘못된 값으로 요청할 때
     */
    @ExceptionHandler({InvalidValueException.class})
    protected ResponseEntity<ApiCallResponse<?>> handleInvalidValueException(InvalidValueException e) {
        final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
        logger.warn(log);

        final var response = ApiCallResponse.of(ApiResultCode.BAD_REQUEST, e.messageToClient, null);
        return new ResponseEntity<>(response, e.apiResultCode.httpStatus.toHttpStatus());
    }

    /**
     * 권한 없는 이용자가 요청할 때
     */
    @ExceptionHandler({IllegalAccessException.class})
    protected ResponseEntity<ApiCallResponse<?>> handleUnauthorizedException(IllegalAccessException e) {
        final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
        logger.warn(log);

        final var response = ApiCallResponse.of(ApiResultCode.ILLEGAL_ACCESS, e.messageToClient, null);
        return new ResponseEntity<>(response, e.apiResultCode.httpStatus.toHttpStatus());
    }

    /**
     * Validation 이 실패할 때
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<ApiCallResponse<?>> handleMethodArgumentNotValid(ConstraintViolationException e) {
        logger.warn("400 Exception occurs. " + e.getMessage());

        final var response = ApiCallResponse.of(ApiResultCode.BAD_REQUEST, e.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 요청한 URI 가 없을 때
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        logger.warn("404 Exception occurs. URI=" + ex.getRequestURL());
        return ResponseEntity.of(Optional.of(ApiCallResponse.of(ApiResultCode.BAD_REQUEST, null)));
    }

    /**
     * 서버 내부의 문제일 때
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    protected ApiCallResponse<Void> handleBaseException(Exception e) {
        logger.error("Unexpected Exception occurs", e);
        return ApiCallResponse.of(ApiResultCode.SERVER_ERROR, null);
    }
}