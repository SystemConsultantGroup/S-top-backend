package com.scg.stop.proposal.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.file.dto.response.FileResponse;
import com.scg.stop.project.domain.ProjectType;
import com.scg.stop.proposal.domain.request.CreateProposalRequest;
import com.scg.stop.proposal.domain.request.ProposalReplyRequest;
import com.scg.stop.proposal.domain.response.ProposalDetailResponse;
import com.scg.stop.proposal.domain.response.ProposalReplyResponse;
import com.scg.stop.proposal.domain.response.ProposalResponse;
import com.scg.stop.proposal.service.ProposalService;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import jakarta.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ProposalController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class ProposalControllerTest extends AbstractControllerTest {

    private static final String ADMIN_ACCESS_TOKEN = "admin_access_token";
    private static final String ADMIN_OR_COMPANY_ACCESS_TOKEN = "admin_or_company_access_token";
    private static final String ALL_ACCESS_TOKEN = "all_user_access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProposalService proposalService;

    @BeforeEach
    void setUp() {
        User adminUser = new User("admin");
        adminUser.register(
                "어드민",
                "email.com",
                "phone",
                UserType.ADMIN,
                "temp"
        );
//        when(authUserArgumentResolver.supportsParameter(any())).thenReturn(true);
        doCallRealMethod().when(authUserArgumentResolver).supportsParameter(any());
        when(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(adminUser);
    }
    @Test
    @DisplayName("과제제안 리스트를 조회할 수 있다")
    void getProposals() throws Exception {
        //given
        ProposalResponse proposal1 = ProposalResponse.of(1L, "과제 제안1", "이름", LocalDateTime.now());
        ProposalResponse proposal2 = ProposalResponse.of(2L, "과제 제안2", "이름", LocalDateTime.now());

        Page<ProposalResponse> proposalPage = new PageImpl<>(
                List.of(proposal1, proposal2),
                PageRequest.of(0, 10), 2
        );
        when(proposalService.getProposalList(any(), any(), any(), any())).thenReturn(proposalPage);

        //when
        ResultActions result = mockMvc.perform(get(
                "/proposals")
                .header(HttpHeaders.AUTHORIZATION, ALL_ACCESS_TOKEN)
                .cookie(new Cookie("refresh-token", REFRESH_TOKEN)));

        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("scope").description("필터 조건 (title,content,both,author)").optional(),
                                parameterWithName("term").description("필터 키워드").optional(),
                                parameterWithName("page").description("페이지 번호 [default = 0]").optional(),
                                parameterWithName("size").description("페이지 크기 [default = 10]").optional()
                        ),
                        responseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("과제 제안 ID"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("과제 제안 제목"),
                                fieldWithPath("content[].name").type(JsonFieldType.STRING).description("과제 제안 작성자"),
                                fieldWithPath("content[].createdDate").type(JsonFieldType.STRING).description("과제 제안 생성일"),
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
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("비어있는 페이지 여부")
                        )
                ));
    }

    @Test
    @DisplayName("과제 제안의 상세 페이지를 조회할 수 있다.")
    void getProposalDetail() throws Exception {
        //given
        List<FileResponse> fileResponses = Arrays.asList(
                new FileResponse(1L, "uuid1", "과제제안서 이름1.pdf", "application/pdf", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(2L, "uuid2", "과제제안서 이름2.pdf", "application/pdf", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(3L, "uuid3", "과제제안서 이름3.pdf", "application/pdf", LocalDateTime.now(), LocalDateTime.now())
        );

        ProposalDetailResponse proposalDetailResponse = new ProposalDetailResponse(1L,
                "작성자",
                "작성자@email.com",
                "website.com",
                "과제 제안 제목",
                List.of(ProjectType.LAB, ProjectType.CLUB, ProjectType.STARTUP, ProjectType.RESEARCH_AND_BUSINESS_FOUNDATION),
                "과제제안 내용",
                false,
                fileResponses
        );

        when(proposalService.getProposalDetail(any(), any())).thenReturn(proposalDetailResponse);

        //when
        ResultActions result = mockMvc.perform(get("/proposals/{proposalId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, ADMIN_OR_COMPANY_ACCESS_TOKEN)
                .cookie(new Cookie("refresh-token", REFRESH_TOKEN)));

        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("proposalId").description("수정할 과제제안 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("과제 제안 ID"),
                                fieldWithPath("authorName").type(JsonFieldType.STRING).description("과제 제안 작성자"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("과제 제안 이메일"),
                                fieldWithPath("webSite").type(JsonFieldType.STRING).description("과제 제안 사이트"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("과제 제안 제목"),
                                fieldWithPath("projectTypes").type(JsonFieldType.ARRAY).description("과제 제안 프로젝트 유형들"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("과제 제안 내용"),
                                fieldWithPath("replied").type(JsonFieldType.BOOLEAN).description("과제 제안 답변 유무"),
                                fieldWithPath("files").type(JsonFieldType.ARRAY).description("파일 목록"),
                                fieldWithPath("files[].id").type(JsonFieldType.NUMBER).description("파일 ID"),
                                fieldWithPath("files[].uuid").type(JsonFieldType.STRING).description("파일 UUID"),
                                fieldWithPath("files[].name").type(JsonFieldType.STRING).description("파일 이름"),
                                fieldWithPath("files[].mimeType").type(JsonFieldType.STRING).description("파일 MIME 타입"),
                                fieldWithPath("files[].createdAt").type(JsonFieldType.STRING).description("파일 생성일"),
                                fieldWithPath("files[].updatedAt").type(JsonFieldType.STRING).description("파일 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("과제 제안을 생성할 수 있다.")
    void createProposal() throws Exception {
        CreateProposalRequest createProposalRequest = new CreateProposalRequest(
                "website.com",
                "과제제안제목",
                "이메일@email.com",
                List.of(ProjectType.LAB, ProjectType.CLUB),
                "과제제안내용",
                "true", "true",
                List.of(1L, 2L, 3L)
        );
        List<FileResponse> fileResponses = Arrays.asList(
                new FileResponse(1L, "uuid1", "과제제안서 이름1.pdf", "application/pdf", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(2L, "uuid2", "과제제안서 이름2.pdf", "application/pdf", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(3L, "uuid3", "과제제안서 이름3.pdf", "application/pdf", LocalDateTime.now(), LocalDateTime.now())
        );
        ProposalDetailResponse proposalDetailResponse = new ProposalDetailResponse(
                1L,
                "작성자",
                "이메일@email.com",
                "website.com",
                "과제제안제목",
                List.of(ProjectType.LAB, ProjectType.CLUB),
                "과제제안내용",
                false,
                fileResponses
        );
        when(proposalService.createProposal(any(), any())).thenReturn(proposalDetailResponse);

        ResultActions result = mockMvc.perform(post("/proposals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createProposalRequest))
                .header(HttpHeaders.AUTHORIZATION, ADMIN_OR_COMPANY_ACCESS_TOKEN)
                .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        //then
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
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("과제제안 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("과제제안 내용"),
                                fieldWithPath("webSite").type(JsonFieldType.STRING).description("과제제안 사이트").optional(),
                                fieldWithPath("projectTypes").type(JsonFieldType.ARRAY).description("과제제안 프로젝트 유형:"
                                        + "RESEARCH_AND_BUSINESS_FOUNDATION, LAB, STARTUP, CLUB"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("과제제안 이메일"),
                                fieldWithPath("isVisible").type(JsonFieldType.BOOLEAN).description("공개 여부"),
                                fieldWithPath("isAnonymous").type(JsonFieldType.BOOLEAN).description("익명 여부"),
                                fieldWithPath("fileIds").type(JsonFieldType.ARRAY).description("파일 id 리스트")

                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("과제 제안 ID"),
                                fieldWithPath("authorName").type(JsonFieldType.STRING).description("과제 제안 작성자"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("과제 제안 이메일"),
                                fieldWithPath("webSite").type(JsonFieldType.STRING).description("과제 제안 사이트"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("과제 제안 제목"),
                                fieldWithPath("projectTypes").type(JsonFieldType.ARRAY).description("과제 제안 프로젝트 유형들"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("과제 제안 내용"),
                                fieldWithPath("replied").type(JsonFieldType.BOOLEAN).description("과제 제안 답변 유무"),fieldWithPath("files").type(JsonFieldType.ARRAY).description("파일 목록"),
                                fieldWithPath("files[].id").type(JsonFieldType.NUMBER).description("파일 ID"),
                                fieldWithPath("files[].uuid").type(JsonFieldType.STRING).description("파일 UUID"),
                                fieldWithPath("files[].name").type(JsonFieldType.STRING).description("파일 이름"),
                                fieldWithPath("files[].mimeType").type(JsonFieldType.STRING).description("파일 MIME 타입"),
                                fieldWithPath("files[].createdAt").type(JsonFieldType.STRING).description("파일 생성일"),
                                fieldWithPath("files[].updatedAt").type(JsonFieldType.STRING).description("파일 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("과제 제안을 수정할 수 있다.")
    void updateProposal() throws Exception{
        CreateProposalRequest updateProposalRequest = new CreateProposalRequest(
                "website.com",
                "과제제안제목",
                "이메일@email.com",
                 List.of(ProjectType.LAB, ProjectType.CLUB),
                "과제제안내용",
                "true",
                "true",
                List.of(1L,2L,3L)
        );
        List<FileResponse> fileResponses = Arrays.asList(
                new FileResponse(1L, "uuid1", "과제제안서 이름1.pdf", "application/pdf", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(2L, "uuid2", "과제제안서 이름2.pdf", "application/pdf", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(3L, "uuid3", "과제제안서 이름3.pdf", "application/pdf", LocalDateTime.now(), LocalDateTime.now())
        );
        ProposalDetailResponse proposalDetailResponse = new ProposalDetailResponse(
                1L,
                "작성자",
                "이메일@email.com",
                "website.com",
                "과제제안제목",
                List.of(ProjectType.LAB, ProjectType.CLUB),
                "과제제안내용",
                false,
                fileResponses
        );
        when(proposalService.updateProposal(any(), any(), any())).thenReturn(proposalDetailResponse);

        ResultActions result = mockMvc.perform(put("/proposals/{proposalId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateProposalRequest))
                .header(HttpHeaders.AUTHORIZATION, ADMIN_OR_COMPANY_ACCESS_TOKEN)
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
                        pathParameters(
                                parameterWithName("proposalId").description("수정할 과제제안 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("과제제안 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("과제제안 내용"),
                                fieldWithPath("webSite").type(JsonFieldType.STRING).description("과제제안 사이트").optional(),
                                fieldWithPath("projectTypes").type(JsonFieldType.ARRAY).description("과제제안 프로젝트 유형:" + "RESEARCH_AND_BUSINESS_FOUNDATION, LAB, STARTUP, CLUB"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("과제제안 이메일"),
                                fieldWithPath("isVisible").type(JsonFieldType.BOOLEAN).description("공개 여부"),
                                fieldWithPath("isAnonymous").type(JsonFieldType.BOOLEAN).description("익명 여부"),
                                fieldWithPath("fileIds").type(JsonFieldType.ARRAY).description("파일 id 리스트")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("과제 제안 ID"),
                                fieldWithPath("authorName").type(JsonFieldType.STRING).description("과제 제안 작성자"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("과제 제안 이메일"),
                                fieldWithPath("webSite").type(JsonFieldType.STRING).description("과제 제안 사이트"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("과제 제안 제목"),
                                fieldWithPath("projectTypes").type(JsonFieldType.ARRAY).description("과제 제안 프로젝트 유형들"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("과제 제안 내용"),
                                fieldWithPath("replied").type(JsonFieldType.BOOLEAN).description("과제 제안 답변 유무"),
                                fieldWithPath("files").type(JsonFieldType.ARRAY).description("파일 목록"),
                                fieldWithPath("files[].id").type(JsonFieldType.NUMBER).description("파일 ID"),
                                fieldWithPath("files[].uuid").type(JsonFieldType.STRING).description("파일 UUID"),
                                fieldWithPath("files[].name").type(JsonFieldType.STRING).description("파일 이름"),
                                fieldWithPath("files[].mimeType").type(JsonFieldType.STRING).description("파일 MIME 타입"),
                                fieldWithPath("files[].createdAt").type(JsonFieldType.STRING).description("파일 생성일"),
                                fieldWithPath("files[].updatedAt").type(JsonFieldType.STRING).description("파일 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("과제 제안을 삭제 할 수 있다.")
    void deleteProposal() throws Exception{
        //given
        doNothing().when(proposalService).deleteProposal(any(), any());

        // when
        ResultActions result = mockMvc.perform(
                delete("/proposals/{proposalId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_OR_COMPANY_ACCESS_TOKEN)
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
                                parameterWithName("proposalId").description("삭제할 과제제안 ID")
                        )
                ));
    }

    @Test
    @DisplayName("과제 제안 답변을 작성할 수 있다.")
    void createProposalReply() throws Exception{
        ProposalReplyRequest proposalReplyRequest = new ProposalReplyRequest("답변 제목", "답변 내용");
        ProposalReplyResponse proposalReplyResponse = ProposalReplyResponse.of(1L, "답변 제목", "답변 내용");

        when(proposalService.createProposalReply(any(), any())).thenReturn(proposalReplyResponse);

        ResultActions result = mockMvc.perform(post("/proposals/{proposalId}/reply", 1L)
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, ADMIN_OR_COMPANY_ACCESS_TOKEN)
                .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                .content(objectMapper.writeValueAsString(proposalReplyRequest))
        );

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
                                parameterWithName("proposalId").description("해당 과제제안 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("과제제안 답변 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("과제제안 답변 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("과제 제안 답변 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("과제 제안 답변 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("과제 제안 답변 내용")
                        )
                ));
    }

    @Test
    @DisplayName("과제 제안 답변을 수정할 수 있다.")
    void updateProposalReply() throws Exception {
        ProposalReplyRequest proposalReplyRequest = new ProposalReplyRequest("답변 제목", "답변 내용");
        ProposalReplyResponse proposalReplyResponse = ProposalReplyResponse.of(1L, "답변 제목", "답변 내용");

        when(proposalService.updateProposalReply(any(), any())).thenReturn(proposalReplyResponse);

        ResultActions result = mockMvc.perform(put("/proposals/{proposalId}/reply/{replyId}", 1L, 1L)
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, ADMIN_OR_COMPANY_ACCESS_TOKEN)
                .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                .content(objectMapper.writeValueAsString(proposalReplyRequest))
        );

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
                                parameterWithName("proposalId").description("해당 과제제안 ID"),
                                parameterWithName("replyId").description("과제 제안 답변 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("과제제안 답변 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("과제제안 답변 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("과제 제안 답변 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("과제 제안 답변 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("과제 제안 답변 내용")
                        )
                ));
    }

    @Test
    @DisplayName("과제 제안 답변을 삭제할 수 있다.")
    void deleteProposalReply() throws Exception{
        //given
        doNothing().when(proposalService).deleteProposalReply(any());

        // when
        ResultActions result = mockMvc.perform(
                delete("/proposals/{proposalId}/reply/{proposalReplyId}", 1L, 1L)
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
                                parameterWithName("proposalId").description("해당 과제제안 ID"),
                                parameterWithName("proposalReplyId").description("삭제할 과제제안 답변 ID")
                        )
                ));
    }

    @Test
    @DisplayName("과제제안 답변을 조회할 수 있다.")
    void getProposalReplies() throws Exception{
        //given
        ProposalReplyResponse proposalReplyResponse = ProposalReplyResponse.of(1L, "과제제안 답변 제목", "과제제안 답변 내용");

        when(proposalService.getProposalReply(any(), any())).thenReturn(proposalReplyResponse);

        // when
        ResultActions result = mockMvc.perform(
                get("/proposals/{proposalId}/reply", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_OR_COMPANY_ACCESS_TOKEN)
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
                                parameterWithName("proposalId").description("해당 과제제안 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("과제 제안 답변 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("과제 제안 답변 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("과제 제안 답변 내용")
                        )
                ));
    }


}
