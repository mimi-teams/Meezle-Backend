package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.dto.ApiResponse;
import com.mimi.w2m.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * BaseGenericApi(DTO : Req/Res DTO Type, PK : Primary Key Type, SV : Service Type)
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/26
 **/
@Tag(name = "Generic CRUD", description = "모든 API 에 공통으로 사용되는 CRUD 일반화")
@RequiredArgsConstructor
@RequestMapping(path="/meezle/api/v1")
public abstract class BaseGenericApi<REQ_DTO, RES_DTO, PK, SV> {
protected final SV service;
protected final AuthService authService;
protected final HttpSession httpSession;

@Operation(method = "GET", description = "[인증] ID의 DTO 가져오기(로그인한 사용자는 모두 가능. No Authorization)")
@GetMapping(path = "/{id}")
public abstract ApiResponse<RES_DTO> get(
        @PathVariable("id") PK id);

@Operation(method = "POST", description = "[인증] DTO 등록하기")
@PostMapping(path = "")
public abstract ApiResponse<RES_DTO> post(
        @RequestBody REQ_DTO reqDto);

@Operation(method = "PATCH", description = "[인증] DTO 일부 수정하기(반환 없음)")
@PatchMapping(path = "/{id}")
public abstract ApiResponse<RES_DTO> patch(
        @PathVariable("id") PK id,
        @RequestBody REQ_DTO reqDto);

@Operation(method = "PUT", description = "[인증] DTO 교체(PATCH) or 생성(POST)하기")
@PutMapping(path = "/{id}")
public abstract ApiResponse<RES_DTO> put(
        @PathVariable("id") PK id,
        @RequestBody REQ_DTO reqDto);

@Operation(method = "DELETE", description = "[인증] DTO 삭제('/'로 Redirect)")
@DeleteMapping(path = "/{id}")
public abstract ResponseEntity<?> delete(
        @PathVariable("id") PK id);

}