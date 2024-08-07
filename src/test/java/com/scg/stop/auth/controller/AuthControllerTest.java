package com.scg.stop.auth.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.auth.JwtUtil;
import com.scg.stop.auth.domain.StudentInfoDto;
import com.scg.stop.auth.domain.UserToken;
import com.scg.stop.auth.domain.request.RegisterRequest;
import com.scg.stop.auth.domain.response.AccessTokenResponse;
import com.scg.stop.auth.domain.response.RegisterResponse;
import com.scg.stop.auth.service.AuthService;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureRestDocs
public class AuthControllerTest extends AbstractControllerTest {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String REISSUED_ACCESS_TOKEN = "reissued_access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String accessCode = "codefromkakaologin";

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthService authService;

    @MockBean
    JwtUtil jwtUtil;

    @Test
    @DisplayName("카카오 소셜 로그인을 할 수 있다.")
    void kakaoSocialLogin() throws Exception {
        //given & when

        when(authService.login(any()))
                .thenReturn(new UserToken(ACCESS_TOKEN, REFRESH_TOKEN));

        MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.get("/auth/login/kakao?code=codefromkakaologin"))
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("code").description("카카오 인가코드")
                        ),
                        responseFields(
                                fieldWithPath("accessToken")
                                        .type(STRING)
                                        .description("access token")
                        )
                ))
                .andReturn();
        //then
        AccessTokenResponse accessTokenResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                AccessTokenResponse.class
        );

        assertThat(accessTokenResponse.getAccessToken()).isEqualTo(ACCESS_TOKEN);
    }

    @Test
    @DisplayName("회원가입을 통해 추가 정보를 입력할 수 있다.")
    void register() throws Exception {
        //given
        StudentInfoDto studentInfoDto = new StudentInfoDto("소프트웨어학과", "2021123123");
        RegisterRequest request = new RegisterRequest("stop-user", "010-1234-1234", UserType.STUDENT, "email@gmail.com",
                "ad", studentInfoDto, null, null);
        User user = new User("1");
        user.register("stop-user", "email@email.com", "010-1234-1234", UserType.STUDENT, "signupsource");
        RegisterResponse registerResponse = RegisterResponse.from(user);
        when(authService.finishRegister(any(), any()))
                .thenReturn(registerResponse);
        UserToken userToken = new UserToken(ACCESS_TOKEN, REFRESH_TOKEN);

        //when
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token")
                        ),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("유저 이름"),
                                fieldWithPath("phoneNumber").type(STRING).description("전화 번호"),
                                fieldWithPath("userType").type(STRING).description("회원 유형"),
                                fieldWithPath("email").type(STRING).description("이메일"),
                                fieldWithPath("signUpSource").type(STRING).description("가입 경로").optional(),
                                fieldWithPath("studentInfo.department").type(STRING).description("학과"),
                                fieldWithPath("studentInfo.studentNumber").type(STRING).description("학번"),
                                fieldWithPath("division").type(STRING).description("소속").optional(),
                                fieldWithPath("position").type(STRING).description("직책").optional()
                        ),
                        responseFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("phone").description("전화번호")
                        )
                ));
        //then

    }

    @Test
    @DisplayName("Access Token을 재발급받을 수 있다.")
    void reissueToken() throws Exception {
        //given
        when(authService.reissueAccessToken(REFRESH_TOKEN, ACCESS_TOKEN))
                .thenReturn(REISSUED_ACCESS_TOKEN);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN)))
                .andExpect(status().isOk())
                .andReturn();

        AccessTokenResponse accessTokenResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                AccessTokenResponse.class
        );

        //then
        assertThat(accessTokenResponse.getAccessToken()).isEqualTo(REISSUED_ACCESS_TOKEN);
    }

    @Test
    @DisplayName("로그아웃할 수 있다.")
    void logout() throws Exception {
        //given
        Mockito.doNothing().when(authService).logout(any());

        //when
        mockMvc.perform(post("/auth/logout")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN)))
                .andExpect(status().isNoContent());

        //then
        Mockito.verify(authService).logout(any());
    }
}
