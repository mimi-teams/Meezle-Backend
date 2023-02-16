package com.mimi.w2m.backend.config.swagger;

import com.mimi.w2m.backend.config.constants.Constants;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * SpringSwaggerConfig(LocalDateTime 의 Swagger Default Format 이 DateTime 이고, 둘 간 불일치를 해결하기 위해 Custom 을 함)
 *
 * @author teddy, yeh35
 * @since 2022.12.25
 **/
@OpenAPIDefinition(
        info = @Info(title = "Meezzle 서버 API 명세서"),
        servers = {@Server(url = "/api")},
        security = {@SecurityRequirement(name = Constants.OPENAPI_SECURITY_BEARER_TOKEN_NAME)}
)
@SecurityScheme(
        name = Constants.OPENAPI_SECURITY_BEARER_TOKEN_NAME,
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class SpringSwaggerConfig {
}