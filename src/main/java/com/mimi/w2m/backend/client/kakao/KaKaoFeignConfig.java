package com.mimi.w2m.backend.client.kakao;

import com.mimi.w2m.backend.config.exception.BadGatewayException;
import com.mimi.w2m.backend.config.exception.InvalidValueException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class KaKaoFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new KakaoApiErrorDecoder();
    }

    /**
     * <a href="https://developers.kakao.com/docs/latest/ko/reference/rest-api-reference#response-code">카카오 에러 문서</a>
     */
    private static class KakaoApiErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(
                String methodKey,
                Response response
        ) {
            FeignException exception = FeignException.errorStatus(methodKey, response);

            //TODO 에러 값 출력

            switch (response.status()) {
                case 400 -> throw new InvalidValueException(String.format(
                        "카카오 API 호출 중 필수 파라미터가 요청되지 않았습니다. status: (%s)" +
                                " message: (%s)", response.status(), response.body()));
                case 401, 403 -> throw new InvalidValueException(String.format(
                        "카카오 API 호출 중 잘못된 토큰이 입력되었습니다. status: (%s) " +
                                "message: (%s)", response.status(), response.body()));
                default -> throw new BadGatewayException(String.format("카카오 API 호출중 에러(%s)가 발생하였습니다. message: (%s) ",
                        response.status(), exception.getMessage()));
            }
        }

    }
}
