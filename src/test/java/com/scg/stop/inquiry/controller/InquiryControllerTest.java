package com.scg.stop.inquiry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.project.controller.InquiryController;
import com.scg.stop.project.controller.ProjectController;
import com.scg.stop.project.dto.request.InquiryReplyRequest;
import com.scg.stop.project.dto.request.InquiryRequest;
import com.scg.stop.project.dto.response.InquiryDetailResponse;
import com.scg.stop.project.dto.response.InquiryReplyResponse;
import com.scg.stop.project.dto.response.InquiryResponse;
import com.scg.stop.project.service.InquiryService;
import com.scg.stop.project.service.ProjectService;
import com.scg.stop.user.domain.User;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        InquiryController.class,
        ProjectController.class
})
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class InquiryControllerTest extends AbstractControllerTest {

    private static final String ADMIN_ACCESS_TOKEN = "admin_access_token";
    private static final String COMPANY_ACCESS_TOKEN = "company_access_token";
    private static final String REFRESH_TOKEN = "refresh_token";


    @MockBean
    private InquiryService inquiryService;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("프로젝트 문의를 생성할 수 있다.")
    void createInquiry() throws Exception {
        // given
        InquiryRequest request = new InquiryRequest("프로젝트 문의 사항 제목", "프로젝트 문의 사항 내용");
        InquiryDetailResponse response = InquiryDetailResponse.of(1L, "문의 작성자 이름", 1L, "프로젝트 이름", "문의 사항 제목", "문의 사항 내용", false, LocalDateTime.now(), LocalDateTime.now());

        when(projectService.createProjectInquiry(anyLong(), any(User.class), any(InquiryRequest.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                post("/projects/{projectId}/inquiry", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, COMPANY_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                        .content(objectMapper.writeValueAsString(request))

        );

        // then
        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token")
                        ),
                        pathParameters(
                                parameterWithName("projectId").description("문의할 프로젝트 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("문의 사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("문의 사항 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("문의 사항 ID"),
                                fieldWithPath("authorName").type(JsonFieldType.STRING).description("문의 작성자 이름"),
                                fieldWithPath("projectId").type(JsonFieldType.NUMBER).description("문의 대상 프로젝트 ID"),
                                fieldWithPath("projectName").type(JsonFieldType.STRING).description("문의 대상 프로젝트 이름"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("문의 사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("문의 사항 내용"),
                                fieldWithPath("replied").type(JsonFieldType.BOOLEAN).description("답변 등록 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("문의 사항 생성 시간"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("문의 사항 수정 시간")
                        )
                ));
    }

    @Test
    @DisplayName("프로젝트 문의 리스트를 조회할 수 있다.")
    void getInquiries() throws Exception {

        // given
        InquiryResponse response1 = InquiryResponse.of(1L, "프로젝트 문의 사항 제목", "프로젝트 문의 사항 내용", LocalDateTime.now());
        InquiryResponse response2 = InquiryResponse.of(2L, "프로젝트 문의 사항 제목", "프로젝트 문의 사항 내용", LocalDateTime.now());
        Page<InquiryResponse> page = new PageImpl<>(List.of(response1, response2), PageRequest.of(0, 10), 2);

        when(inquiryService.getInquiryList(any(), any(Pageable.class))).thenReturn(page);
        // when
        ResultActions result = mockMvc.perform(
                get("/inquiries")
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, COMPANY_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
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
                                parameterWithName("title").description("찾고자 하는 공지 사항 제목").optional(),
                                parameterWithName("page").description("페이지 번호 [default: 0]").optional(),
                                parameterWithName("size").description("페이지 크기 [default: 10]").optional()
                        ),
                        responseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("문의 사항 ID"),
                                fieldWithPath("content[].authorName").type(JsonFieldType.STRING).description("문의 작성자 이름"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("문의 사항 제목"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING).description("문의 사항 생성 시간"),
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
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("비어있는 페이지 여부")
                        )
                ));
    }

    @Test
    @DisplayName("프로젝트 문의를 단건 조회 할 수 있다.")
    void getInquiry() throws Exception {

        // given
        InquiryDetailResponse response = InquiryDetailResponse.of(1L, "문의 작성자 이름", 1L, "프로젝트 이름", "문의 사항 제목", "문의 사항 내용", false, LocalDateTime.now(), LocalDateTime.now());

        when(inquiryService.getInquiry(anyLong(), any(User.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                get("/inquiries/{inquiryId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, COMPANY_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
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
                        pathParameters(
                                parameterWithName("inquiryId").description("조회할 문의 사항 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("문의 사항 ID"),
                                fieldWithPath("authorName").type(JsonFieldType.STRING).description("문의 작성자 이름"),
                                fieldWithPath("projectId").type(JsonFieldType.NUMBER).description("문의 대상 프로젝트 ID"),
                                fieldWithPath("projectName").type(JsonFieldType.STRING).description("문의 대상 프로젝트 이름"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("문의 사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("문의 사항 내용"),
                                fieldWithPath("replied").type(JsonFieldType.BOOLEAN).description("답변 등록 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("문의 사항 생성 시간"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("문의 사항 수정 시간")
                        )
                ));

    }

    @Test
    @DisplayName("프로젝트 문의를 수정 할 수 있다.")
    void updateInquiry() throws Exception {

        // given
        InquiryRequest request = new InquiryRequest("수정된 프로젝트 문의 사항 제목", "수정된 프로젝트 문의 사항 내용");
        InquiryDetailResponse response = InquiryDetailResponse.of(1L, "문의 작성자 이름", 1L, "프로젝트 이름", "수정된 문의 사항 제목", "수정된 문의 사항 내용", false ,LocalDateTime.now(), LocalDateTime.now());

        when(inquiryService.updateInquiry(anyLong(), any(User.class), any(InquiryRequest.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                put("/inquiries/{inquiryId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, COMPANY_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                        .content(objectMapper.writeValueAsString(request))

        );

        // then
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
                        pathParameters(
                                parameterWithName("inquiryId").description("수정할 문의 사항 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("문의 사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("문의 사항 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("문의 사항 ID"),
                                fieldWithPath("authorName").type(JsonFieldType.STRING).description("문의 작성자 이름"),
                                fieldWithPath("projectId").type(JsonFieldType.NUMBER).description("문의 대상 프로젝트 ID"),
                                fieldWithPath("projectName").type(JsonFieldType.STRING).description("문의 대상 프로젝트 이름"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정된 문의 사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 문의 사항 내용"),
                                fieldWithPath("replied").type(JsonFieldType.BOOLEAN).description("답변 등록 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("문의 사항 생성 시간"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("문의 사항 수정 시간")
                        )
                ));
    }

    @Test
    @DisplayName("프로젝트 문의를 삭제할 수 있다.")
    void deleteInquiry() throws Exception {

        // given
        doNothing().when(inquiryService).deleteInquiry(anyLong(), any(User.class));


        // when
        ResultActions result = mockMvc.perform(
                delete("/inquiries/{inquiryId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, COMPANY_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
        result.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token")
                        ),
                        pathParameters(
                                parameterWithName("inquiryId").description("삭제할 문의 사항 ID")
                        )
                ));

    }

    @Test
    @DisplayName("프로젝트 문의 답변을 생성할 수 있다.")
    void createInquiryReply() throws Exception {
        // given
        InquiryReplyRequest request = new InquiryReplyRequest("문의 답변 제목", "문의 답변 내용");
        InquiryReplyResponse response = InquiryReplyResponse.of(1L, "문의 답변 제목", "문의 답변 내용");


        when(inquiryService.createInquiryReply(anyLong(), any(InquiryReplyRequest.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                post("/inquiries/{inquiryId}/reply", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                        .content(objectMapper.writeValueAsString(request))

        );

        // then
        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token")
                        ),
                        pathParameters(
                                parameterWithName("inquiryId").description("답변할 문의 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("답변 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("답변 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("답변 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("답변 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("답변 내용")
                        )
                ));
    }

    @Test
    @DisplayName("프로젝트 문의 답변을 조회할 수 있다.")
    void getInquiryReply() throws Exception {

        // given
        InquiryReplyResponse response = InquiryReplyResponse.of(1L, "문의 답변 제목", "문의 답변 내용");

        when(inquiryService.getInquiryReply(anyLong(), any(User.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                get("/inquiries/{inquiryId}/reply", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, COMPANY_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
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
                        pathParameters(
                                parameterWithName("inquiryId").description("조회할 문의 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("답변 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("답변 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("답변 내용")
                        )
                ));
    }

    @Test
    @DisplayName("프로젝트 문의 답변을 수정할 수 있다.")
    void updateInquiryReply() throws Exception {

        // given
        InquiryReplyRequest request = new InquiryReplyRequest("수정된 문의 답변 제목", "수정된 문의 답변 내용");
        InquiryReplyResponse response = InquiryReplyResponse.of(1L, "수정된 문의 답변 제목", "수정된 문의 답변 내용");

        when(inquiryService.updateInquiryReply(anyLong(), any(InquiryReplyRequest.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                put("/inquiries/{inquiryId}/reply", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                        .content(objectMapper.writeValueAsString(request))

        );

        // then
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
                        pathParameters(
                                parameterWithName("inquiryId").description("수정할 답변 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("답변 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("답변 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("답변 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정된 답변 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 답변 내용")
                        )
                ));
    }

    @Test
    @DisplayName("프로젝트 문의 답변을 삭제할 수 있다.")
    void deleteInquiryReply() throws Exception {

        // given
        doNothing().when(inquiryService).deleteInquiryReply(anyLong());

        // when
        ResultActions result = mockMvc.perform(
                delete("/inquiries/{inquiryId}/reply", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
        result.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token")
                        ),
                        pathParameters(
                                parameterWithName("inquiryId").description("삭제할 답변 ID")
                        )
                ));
    }
}
