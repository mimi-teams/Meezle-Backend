package com.mimi.w2m.backend.api.v1;

import com.mimi.w2m.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * BaseGenericApi(DTO : Req/Res DTO Type, PK : Primary Key Type, SV : Service Type)
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/26
 **/
@RequiredArgsConstructor
@RequestMapping(path = "/meezle/api/v1")
public abstract class BaseGenericApi<SV> {
protected final SV          service;
protected final AuthService authService;
protected final HttpSession httpSession;
}