package com.mimi.w2m.backend.config.swagger;

import com.mimi.w2m.backend.config.constants.Constants;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Objects;

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
    private final Logger logger = LoggerFactory.getLogger(SpringSwaggerConfig.class.getName());

    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> {
            openApi.getComponents()
                    .getSchemas()
                    .forEach((s, schema) -> {
                        var properties = schema.getProperties();
                        if (Objects.isNull(properties)) {
                            properties = Map.of();
                        }
                        for (var propertyName : properties.keySet()) {
                            final var propertySchema = properties.get(propertyName);
                            if (propertySchema instanceof DateTimeSchema) {
                                properties.replace(propertyName, new StringSchema().example("2022-05-30T12:00:00.000")
                                        .pattern("^\\d{4}-\\d{2}-\\d{2}T\\d" +
                                                "{2}:\\d{2}:\\d{2}\\" +
                                                ".\\d{3}$")
                                        .description("날짜 및 시각")
                                        .nullable(true));
                            }
                        }
                    });
        };
    }
}