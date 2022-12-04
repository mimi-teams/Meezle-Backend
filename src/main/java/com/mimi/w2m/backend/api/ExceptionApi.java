package com.mimi.w2m.backend.api;

import com.mimi.w2m.backend.type.response.ApiCallResponse;
import com.mimi.w2m.backend.type.response.ApiResultCode;
import com.mimi.w2m.backend.type.response.exception.EntityDuplicatedException;
import com.mimi.w2m.backend.type.response.exception.EntityNotFoundException;
import com.mimi.w2m.backend.type.response.exception.IllegalAccessException;
import com.mimi.w2m.backend.type.response.exception.InvalidValueException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * ExceptionApi
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/27
 **/
@Tag(name = "Exception Api", description = "요청 시 발생 가능한 예외 사항에 대한 반환 값을 다룬다")
@RestControllerAdvice
public class ExceptionApi extends ResponseEntityExceptionHandler {
    private final Logger    logger    = LoggerFactory.getLogger(ExceptionApi.class.getName());
    private final Formatter formatter = new Formatter();

    @ApiResponse(description = "요청된 정보가 중복된 것일 때, 반환된다", useReturnTypeSchema = true)
    @ExceptionHandler({EntityDuplicatedException.class})
    protected ApiCallResponse<Void> handleEntityDuplicatedException(EntityDuplicatedException e) {
        final var log = formatter.format("[%s] : %s", e.getClass(), e.message)
                                 .toString();
        logger.warn(log);
        return ApiCallResponse.of(ApiResultCode.ENTITY_DUPLICATED, e.messageToClient, null);
    }

    @ApiResponse(description = "요청된 정보가 없을 때, 반환된다", useReturnTypeSchema = true)
    @ExceptionHandler({EntityNotFoundException.class})
    protected ApiCallResponse<Void> handleEntityNotFoundException(EntityNotFoundException e) {
        final var log = formatter.format("[%s] : %s", e.getClass(), e.message)
                                 .toString();
        logger.warn(log);
        return ApiCallResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
    }

    @ApiResponse(description = "잘못된 값으로 요청할 때, 반환된다", useReturnTypeSchema = true)
    @ExceptionHandler({InvalidValueException.class})
    protected ApiCallResponse<Void> handleInvalidValueException(InvalidValueException e) {
        final var log = formatter.format("[%s] : %s", e.getClass(), e.message)
                                 .toString();
        logger.warn(log);
        return ApiCallResponse.of(ApiResultCode.BAD_REQUEST, e.messageToClient, null);
    }

    @ApiResponse(description = "권한 없는 이용자가 요청할 때, 반환된다", useReturnTypeSchema = true)
    @ExceptionHandler({IllegalAccessException.class})
    protected ApiCallResponse<Void> handleUnauthorizedException(IllegalAccessException e) {
        final var log = formatter.format("[%s] : %s", e.getClass(), e.message)
                                 .toString();
        logger.warn(log);
        return ApiCallResponse.of(ApiResultCode.ILLEGAL_ACCESS, e.messageToClient, null);
    }

    @ApiResponse(description = "Validation 이 실패할 때, 반환된다", useReturnTypeSchema = true)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    protected ApiCallResponse<Void> handleMethodArgumentNotValid(ConstraintViolationException e) {
        logger.warn("400 Exception occurs. " + e.getMessage());
        return ApiCallResponse.of(ApiResultCode.BAD_REQUEST, null);
    }

    @ApiResponse(description = "요청한 URI 가 없을 때, 반환된다", useReturnTypeSchema = true)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        logger.warn("404 Exception occurs. URI=" + ex.getRequestURL());
        return ResponseEntity.of(Optional.of(ApiCallResponse.of(ApiResultCode.BAD_REQUEST, null)));
    }

    @ApiResponse(description = "서버 내부의 문제일 때, 반환된다", useReturnTypeSchema = true)
    @ExceptionHandler({Exception.class})
    protected ApiCallResponse<Void> handleBaseException(Exception e) {
        logger.error("Unexpected Exception occurs", e);
        //        Arrays.stream(e.getStackTrace())
        //              .toList()
        //              .forEach(logger::warn);
        return ApiCallResponse.of(ApiResultCode.SERVER_ERROR, null);
    }
}