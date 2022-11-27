package com.mimi.w2m.backend.config.web.mapping;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class SpringWebRequestMappingConfig extends DelegatingWebMvcConfiguration {
@Override
protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
    return new CustomRequestMappingHandler();
}
}