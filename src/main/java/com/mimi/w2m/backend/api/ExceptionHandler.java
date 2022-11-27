package com.mimi.w2m.backend.api;

import com.mimi.w2m.backend.dto.ApiResponse;
import com.mimi.w2m.backend.dto.ApiResultCode;
import com.mimi.w2m.backend.error.EntityDuplicatedException;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.InvalidValueException;
import com.mimi.w2m.backend.error.UnauthorizedException;
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
protected ApiResponse<?> handleEntityDuplicatedException(EntityDuplicatedException e) {
    final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
    logger.warn(log);
    return ApiResponse.of(ApiResultCode.ENTITY_DUPLICATED, e.messageToClient, null);
}

@org.springframework.web.bind.annotation.ExceptionHandler({EntityNotFoundException.class})
protected ApiResponse<?> handleEntityNotFoundException(EntityNotFoundException e) {
    final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
    logger.warn(log);
    return ApiResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
}

@org.springframework.web.bind.annotation.ExceptionHandler({InvalidValueException.class})
protected ApiResponse<?> handleInvalidValueException(InvalidValueException e) {
    final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
    logger.warn(log);
    return ApiResponse.of(ApiResultCode.INVALID_VALUE, e.messageToClient, null);
}

@org.springframework.web.bind.annotation.ExceptionHandler({UnauthorizedException.class})
protected ApiResponse<?> handleUnauthorizedException(UnauthorizedException e) {
    final var log = formatter.format("[%s] : %s", e.getClass(), e.message).toString();
    logger.warn(log);
    return ApiResponse.of(ApiResultCode.UNAUTHORIZED, e.messageToClient, null);
}

@org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
protected ApiResponse<?> handleBaseException(Exception e) {
    logger.error("Unexpected Exception occurs");
    logger.error(e.getMessage());
    Arrays.stream(e.getStackTrace()).toList().forEach(logger::error);
    return ApiResponse.of(ApiResultCode.SERVER_ERROR, null);
}
}