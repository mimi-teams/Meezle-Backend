package com.mimi.w2m.backend.config.web.mapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CustomRequestMappingHandler(RequestMapping 상속 구현")
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/26
 **/
public class CustomRequestMappingHandler extends RequestMappingHandlerMapping {
private final Logger logger = LogManager.getLogger(CustomRequestMappingHandler.class);
@Override
protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
    final var requestMappingInfo = super.getMappingForMethod(method, handlerType);

    // URL 정보가 없는 경우, null 을 반환
    if(Objects.isNull(requestMappingInfo)) {
        return null;
    }

    // [root, ..., super] 순서로 URL 저장
    var superclassUrlPatterns = new ArrayList<String>();
    for(var superclass = handlerType.getSuperclass();
        !Objects.equals(superclass, Object.class);
        superclass = superclass.getSuperclass()) {
        if(superclass.isAnnotationPresent(RequestMapping.class)) {
            // @RequestMapping(path=[url]) 로 경로를 지정한다
            superclassUrlPatterns.add(0, superclass.getAnnotation(RequestMapping.class).path()[0]);
        }
    }
    // superclass 의 url sequence 가 있는 경우, 조합 : root + "/" + sub ...
    // 각 uri는 양 끝에 "/"를 적던 말던 상관 없다
    if(!superclassUrlPatterns.isEmpty()) {
        final var path = String.join("", superclassUrlPatterns);
        final var superclassRequestMappingInfo =
                RequestMappingInfo.paths(path).build();
        return superclassRequestMappingInfo.combine(requestMappingInfo);
    } else {
        return requestMappingInfo;
    }
}

/**
 * /root/.../final 형식으로 연결한다
 *
 * @author teddy
 * @since 2022/11/26
 **/
private String concatenate(List<String> patterns) {
    // TODO: 2022/11/26
    return null;
}
}