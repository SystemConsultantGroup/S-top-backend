package com.scg.stop.user.controller;

import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import com.scg.stop.user.dto.response.UserResponse;
import com.scg.stop.user.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class UserControllerTest extends AbstractControllerTest {

    private static final String ACCESS_TOKEN = "admin_access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("사용자 정보를 조회할 수 있다.")
    void getMe() throws Exception {
        // given
        UserResponse response = new UserResponse(
                1L,
                "이름",
                "010-1234-5678",
                "student@g.skku.edu",
                "아이디",
                UserType.STUDENT,
                null,
                null,
                "2000123456",
                "학과",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(userService.getMe(any(User.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                get("/users/me")
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("사용자 전화번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                fieldWithPath("socialLoginId").type(JsonFieldType.STRING).description("사용자 이메일"),
                                fieldWithPath("userType").type(JsonFieldType.STRING).description("사용자 유형"),
                                fieldWithPath("division").type(JsonFieldType.STRING).description("소속").optional(),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("직책").optional(),
                                fieldWithPath("studentNumber").type(JsonFieldType.STRING).description("학번").optional(),
                                fieldWithPath("departmentName").type(JsonFieldType.STRING).description("학과 이름").optional(),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("수정일")
                        )
                ));
    }

}
