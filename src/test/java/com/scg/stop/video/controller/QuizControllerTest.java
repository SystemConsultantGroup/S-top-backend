package com.scg.stop.video.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.video.dto.response.UserQuizResultResponse;
import com.scg.stop.video.service.QuizService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuizController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class QuizControllerTest extends AbstractControllerTest {
    @MockBean
    private QuizService quizService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ACCESS_TOKEN = "admin_access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    @Test
    @DisplayName("관리자는 올해의 퀴즈 제출 기록을 가져올 수 있다.")
    void getQuizResults() throws Exception {
        //given
        UserQuizResultResponse response1 = new UserQuizResultResponse(1L, "name", "010-1111-1111","scg@scg.skku.ac.kr", 3L);
        UserQuizResultResponse response2 = new UserQuizResultResponse(2L, "name2", "010-0000-1234", "iam@2tle.io",1L);
        Page<UserQuizResultResponse> pages = new PageImpl<>(List.of(response1, response2), PageRequest.of(0,10), 2);

        when(quizService.getQuizResults(any(), any())).thenReturn(pages);
        //when

        ResultActions result = mockMvc.perform(
                get("/quizzes/result")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        //then

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token")
                        ),
                        queryParameters(
                                parameterWithName("year").description("찾고자 하는 퀴즈 푼 문제의 연도").optional(),
                                parameterWithName("page").description("페이지 번호 [default: 0]").optional(),
                                parameterWithName("size").description("페이지 크기 [default: 10]").optional()
                        ),
                        responseFields(
                                //page
                                fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 당 요소 수"),
                                fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬된 상태인지 여부"),
                                fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않은 상태인지 여부"),
                                fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("오프셋"),
                                fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징된 여부"),
                                fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이징되지 않은 여부"),
                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 요소 수"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 당 요소 수"),
                                fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지 요소 수"),
                                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않은 상태인지 여부"),
                                fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬된 상태인지 여부"),
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("비어있는 페이지 여부"),
                                //content
                                fieldWithPath("content[].user_id").type(JsonFieldType.NUMBER).description("유저의 ID"),
                                fieldWithPath("content[].name").type(JsonFieldType.STRING).description("유저의 이름"),
                                fieldWithPath("content[].phone").type(JsonFieldType.STRING).description("유저의 연락처"),
                                fieldWithPath("content[].email").type(JsonFieldType.STRING).description("유저의 이메일"),
                                fieldWithPath("content[].successCount").type(JsonFieldType.NUMBER).description("유저가 퀴즈를 성공한 횟수의 합")
                        )
                ));
    }
}
