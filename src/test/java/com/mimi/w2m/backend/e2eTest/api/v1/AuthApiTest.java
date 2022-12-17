package com.mimi.w2m.backend.e2eTest.api.v1;

import com.mimi.w2m.backend.e2eTest.End2EndTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
public class AuthApiTest extends End2EndTest {

    @Test
    void OAuth2_이용한_로그인_URL_받기() throws Exception {

        mockMvc.perform(
                        get("/v1/auth/oauth2/authorization")
                                .param("platform", "KAKAO")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("1"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data.authorizationUrl").exists())
        ;

    }


//    @Test
//    void OAuth2_KAKAO_리다이렉션() throws Exception {
//        //given
//        final String mockCode = "1515151251613415";
//
//        //when & then
//        mockMvc.perform(
//                        get("/v1/auth/oauth2/authorization/redirect/kakao")
//                                .param("code", mockCode)
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("1"))
//                .andExpect(jsonPath("$.message").exists())
//                .andExpect(jsonPath("$.data.name").exists())
//                .andExpect(jsonPath("$.data.token").exists())
//        ;
//    }
}
