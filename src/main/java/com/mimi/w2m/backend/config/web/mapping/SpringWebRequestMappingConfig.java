package com.mimi.w2m.backend.config.web.mapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

@Configuration
public class SpringWebRequestMappingConfig extends DelegatingWebMvcConfiguration {
    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new CustomRequestMappingHandler();
    }
}

/**
 * CustomRequestMappingHandler(RequestMapping 상속 구현")
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/26
 **/
class CustomRequestMappingHandler extends RequestMappingHandlerMapping {
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
        for(var superclass = handlerType.getSuperclass(); !Objects.equals(superclass, Object.class);
            superclass = superclass.getSuperclass()) {
            if(superclass.isAnnotationPresent(RequestMapping.class)) {
                // @RequestMapping(path=[url]) 로 경로를 지정한다
                superclassUrlPatterns.add(0, superclass.getAnnotation(RequestMapping.class)
                                                       .path()[0]);
            }
        }
        if(!superclassUrlPatterns.isEmpty()) {
            final var path = String.join("", superclassUrlPatterns);
            final var superclassRequestMappingInfo = RequestMappingInfo.paths(path)
                                                                       .build();
            return superclassRequestMappingInfo.combine(requestMappingInfo);
        } else {
            return requestMappingInfo;
        }
    }
}