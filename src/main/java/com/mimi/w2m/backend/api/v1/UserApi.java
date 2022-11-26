package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.domain.type.Role;
import com.mimi.w2m.backend.dto.ApiResponse;
import com.mimi.w2m.backend.dto.ApiResultCode;
import com.mimi.w2m.backend.dto.user.UserRequestDto;
import com.mimi.w2m.backend.dto.user.UserResponseDto;
import com.mimi.w2m.backend.error.EntityDuplicatedException;
import com.mimi.w2m.backend.error.EntityNotFoundException;
import com.mimi.w2m.backend.error.UnauthorizedException;
import com.mimi.w2m.backend.service.AuthService;
import com.mimi.w2m.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * UserApi
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/26
 **/
@Tag(name = "User Api", description = "User 와 관련된 Api 관리")
@RequestMapping(path = "/users")
@RestController
public class UserApi extends BaseGenericApi<UserRequestDto, UserResponseDto, Long, UserService> {
private final Logger      logger = LogManager.getLogger(UserApi.class);
private final AuthService authService;

public UserApi(UserService service, AuthService authService) {
    super(service);
    this.authService = authService;
}

@Override
public ApiResponse<UserResponseDto> get(
        @PathVariable("id") Long id) {
    try {
        final var user = service.getUser(id);
        return ApiResponse.ofSuccess(UserResponseDto.of(user));
    } catch(EntityNotFoundException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
    } catch(Exception e) {
        logger.error("Unexpected Exception occurs");
        logger.error(e.getMessage());
        Arrays.stream(e.getStackTrace()).toList().forEach(logger::error);
        return ApiResponse.of(ApiResultCode.SERVER_ERROR, ApiResultCode.SERVER_ERROR.defaultMessage, null);
    }
}

@Override
@Deprecated
public ApiResponse<UserResponseDto> post(
        @RequestBody UserRequestDto dto) {
    return ApiResponse.of(ApiResultCode.UNUSED_API, ApiResultCode.UNUSED_API.defaultMessage, null);
}

@Override
public ApiResponse<UserResponseDto> patch(
        @PathVariable Long id,
        @RequestBody UserRequestDto dto) {
    try {
        authService.isCurrentLogin(id, Role.USER);
        final var user = service.updateUser(id, dto.getName(), dto.getEmail());
        return ApiResponse.ofSuccess(null);
    } catch(UnauthorizedException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ILLEGAL_ACCESS, e.messageToClient, null);
    } catch(EntityNotFoundException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
    } catch(EntityDuplicatedException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ENTITY_DUPLICATED, e.messageToClient, null);
    } catch(Exception e) {
        logger.error("Unexpected Exception occurs");
        logger.error(e.getMessage());
        Arrays.stream(e.getStackTrace()).toList().forEach(logger::error);
        return ApiResponse.of(ApiResultCode.SERVER_ERROR, ApiResultCode.SERVER_ERROR.defaultMessage, null);
    }
}

@Override
@Deprecated
public ApiResponse<UserResponseDto> put(
        @PathVariable Long id,
        @RequestBody UserRequestDto dto) {
    return ApiResponse.of(ApiResultCode.UNUSED_API, ApiResultCode.UNUSED_API.defaultMessage, null);
}

// TODO: 2022/11/26 DELETE의 경우, 연관관계인 것까지 모두 처리하기
@Override
public ApiResponse<UserResponseDto> delete(
        @PathVariable Long id) {
    try {
        authService.isCurrentLogin(id, Role.USER);
        service.removeUser(id);
        return ApiResponse.ofSuccess(null);
    } catch(UnauthorizedException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ILLEGAL_ACCESS, e.messageToClient, null);
    } catch(EntityNotFoundException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
    } catch(Exception e) {
        logger.error("Unexpected Exception occurs");
        logger.error(e.getMessage());
        Arrays.stream(e.getStackTrace()).toList().forEach(logger::error);
        return ApiResponse.of(ApiResultCode.SERVER_ERROR, ApiResultCode.SERVER_ERROR.defaultMessage, null);
    }
}

@Operation(method = "GET", description = "[인증] Email 로 이용자 가져오기")
@GetMapping(path = "")
public ApiResponse<UserResponseDto> getByEmail(
        @RequestParam String email) {
    try {
        final var user = service.getUserByEmail(email);
        return ApiResponse.ofSuccess(UserResponseDto.of(user));
    } catch(EntityNotFoundException e) {
        logger.warn(e.message);
        return ApiResponse.of(ApiResultCode.ENTITY_NOT_FOUND, e.messageToClient, null);
    } catch(Exception e) {
        logger.error("Unexpected Exception occurs");
        logger.error(e.getMessage());
        Arrays.stream(e.getStackTrace()).toList().forEach(logger::error);
        return ApiResponse.of(ApiResultCode.SERVER_ERROR, ApiResultCode.SERVER_ERROR.defaultMessage, null);
    }
}

@Operation(method = "GET", description = "[인증X] Oauth Login(platform = google or kakao). 신규 사용자의 경우 새로 등록된다")
@GetMapping("/login")
public ResponseEntity<?> loginWithOauth2(
        @RequestParam String platform, HttpServletResponse response) {
    final var validPlatforms = Set.of("kakao", "google");
    try {
        final var headers = new HttpHeaders();
        if(validPlatforms.contains(platform)) {
            headers.setLocation(URI.create("/oauth2/authorization/" + platform));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        } else {
            return ResponseEntity.of(Optional.of(ApiResponse.of(ApiResultCode.INVALID_VALUE,
                                                                ApiResultCode.INVALID_VALUE.defaultMessage, null)));
        }
    } catch(Exception e) {
        return ResponseEntity.of(Optional.of(ApiResponse.of(ApiResultCode.SERVER_ERROR,
                                                            ApiResultCode.SERVER_ERROR.defaultMessage, null)));
    }
}
}