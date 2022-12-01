package com.mimi.w2m.backend.api;

import com.mimi.w2m.backend.type.response.ApiCallResponse;
import com.mimi.w2m.backend.type.response.ApiResultCode;
import com.mimi.w2m.backend.type.response.exception.EntityDuplicatedException;
import com.mimi.w2m.backend.type.response.exception.EntityNotFoundException;
import com.mimi.w2m.backend.type.response.exception.IllegalAccessException;
import com.mimi.w2m.backend.type.response.exception.InvalidValueException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Formatter;

/**
 * ExceptionApi
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/27
 **/
@Tag(name = "Exception Handler", description = "공통 예외 처리 핸들러")
@RestControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
private final Logger logger = LogManager.getLogger(ExceptionHandler.class);
private final Formatter formatter = new Formatter();

@org.springframework.web.bind.annotation.ExceptionHandler({EntityDuplicatedException.class})
protected ApiCallResponse<?> handleEntityDuplicatedException(EntityDuplicatedException e) {
    final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
    logger.warn(log);
    return ApiCallResponse.of(ApiResultCode.ENTITY_DUPLICATED, e.messageToClient, null);
}

@org.springframework.web.bind.annotation.ExceptionHandler({EntityNotFoundException.class})
protected ApiCallResponse<?> handleEntityNotFoundException(EntityNotFoundException e) {
    final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
    logger.warn(log);
    return ApiCallResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
}

@org.springframework.web.bind.annotation.ExceptionHandler({InvalidValueException.class})
protected ApiCallResponse<?> handleInvalidValueException(InvalidValueException e) {
    final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
    logger.warn(log);
    return ApiCallResponse.of(ApiResultCode.BAD_REQUEST, e.messageToClient, null);
}

@org.springframework.web.bind.annotation.ExceptionHandler({IllegalAccessException.class})
protected ApiCallResponse<?> handleUnauthorizedException(IllegalAccessException e) {
    final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
    logger.warn(log);
    return ApiCallResponse.of(ApiResultCode.ILLEGAL_ACCESS, e.messageToClient, null);
}

@org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
protected ApiCallResponse<?> handleBaseException(Exception e) {
    logger.error("Unexpected Exception occurs");
    logger.error(e.getMessage());
    Arrays.stream(e.getStackTrace()).toList().forEach(logger::error);
    return ApiCallResponse.of(ApiResultCode.SERVER_ERROR, null);
}
}