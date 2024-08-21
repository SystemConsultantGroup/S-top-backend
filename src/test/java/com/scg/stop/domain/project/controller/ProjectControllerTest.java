package com.scg.stop.domain.project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;

import com.scg.stop.domain.project.domain.AwardStatus;
import com.scg.stop.domain.project.domain.ProjectCategory;
import com.scg.stop.domain.project.domain.ProjectType;
import com.scg.stop.domain.project.domain.Role;
import com.scg.stop.domain.project.dto.request.CommentRequest;
import com.scg.stop.domain.project.dto.request.MemberRequest;
import com.scg.stop.domain.project.dto.request.ProjectRequest;
import com.scg.stop.domain.project.dto.response.CommentResponse;
import com.scg.stop.domain.project.dto.response.FileResponse;
import com.scg.stop.domain.project.dto.response.ProjectDetailResponse;
import com.scg.stop.domain.project.dto.response.ProjectResponse;
import com.scg.stop.domain.project.service.ProjectService;
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
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(ProjectController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class ProjectControllerTest extends AbstractControllerTest {

    private static final String ALL_ACCESS_TOKEN = "all_access_token";
    private static final String ADMIN_ACCESS_TOKEN = "admin_access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("간단한 프로젝트 리스트를 조회한다. | 페이지네이션")
    @Test
    void getProjects() throws Exception {
        // given
        ProjectResponse projectResponse1 = new ProjectResponse(
                1L,
                new FileResponse(
                        1L,
                        "썸네일 uuid 1",
                        "썸네일 파일 이름 1",
                        "썸네일 mime 타입 1"
                ),
                "프로젝트 이름 1",
                "팀 이름 1",
                List.of("학생 이름 1", "학생 이름 2"),
                List.of("교수 이름 1"),
                ProjectType.STARTUP,
                ProjectCategory.BIG_DATA_ANALYSIS,
                List.of("파이썬", "SQL"),
                100,
                false,
                false
        );
        ProjectResponse projectResponse2 = new ProjectResponse(
                2L,
                new FileResponse(
                        2L,
                        "썸네일 uuid 2",
                        "썸네일 파일 이름 2",
                        "썸네일 mime 타입 2"
                ),
                "프로젝트 이름 2",
                "팀 이름 2",
                List.of("학생 이름 3", "학생 이름 4"),
                List.of("교수 이름 2"),
                ProjectType.LAB,
                ProjectCategory.AI_MACHINE_LEARNING,
                List.of("파이썬", "OpenCV"),
                100,
                false,
                true
        );
        Page<ProjectResponse> pageResponse = new PageImpl<>(List.of(projectResponse1, projectResponse2), PageRequest.of(0, 10), 2);

        when(projectService.getProjects(any(), any(), any(), any(), any())).thenReturn(pageResponse);

        // when
        ResultActions result = mockMvc.perform(
                get("/projects")
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ALL_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("title").description("프로젝트 이름").optional(),
                                parameterWithName("year").description("프로젝트 년도").optional(),
                                parameterWithName("category").description("프로젝트 카테고리").optional()
                        ),
                        responseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("프로젝트 ID"),
                                fieldWithPath("content[].thumbnailInfo").type(JsonFieldType.OBJECT).description("썸네일 정보"),
                                fieldWithPath("content[].thumbnailInfo.id").type(JsonFieldType.NUMBER).description("썸네일 ID"),
                                fieldWithPath("content[].thumbnailInfo.uuid").type(JsonFieldType.STRING).description("썸네일 UUID"),
                                fieldWithPath("content[].thumbnailInfo.name").type(JsonFieldType.STRING).description("썸네일 파일 이름"),
                                fieldWithPath("content[].thumbnailInfo.mimeType").type(JsonFieldType.STRING).description("썸네일 MIME 타입"),
                                fieldWithPath("content[].projectName").type(JsonFieldType.STRING).description("프로젝트 이름"),
                                fieldWithPath("content[].teamName").type(JsonFieldType.STRING).description("팀 이름"),
                                fieldWithPath("content[].studentNames[]").type(JsonFieldType.ARRAY).description("학생 이름"),
                                fieldWithPath("content[].professorNames[]").type(JsonFieldType.ARRAY).description("교수 이름"),
                                fieldWithPath("content[].projectType").type(JsonFieldType.STRING).description("프로젝트 타입: RESEARCH_AND_BUSINESS_FOUNDATION, LAB, STARTUP, CLUB"),
                                fieldWithPath("content[].projectCategory").type(JsonFieldType.STRING).description("프로젝트 카테고리: COMPUTER_VISION, SYSTEM_NETWORK, WEB_APPLICATION, SECURITY_SOFTWARE_ENGINEERING, NATURAL_LANGUAGE_PROCESSING, BIG_DATA_ANALYSIS, AI_MACHINE_LEARNING, INTERACTION_AUGMENTED_REALITY"),
                                fieldWithPath("content[].techStacks").type(JsonFieldType.ARRAY).description("기술 스택"),
                                fieldWithPath("content[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("content[].like").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
                                fieldWithPath("content[].bookMark").type(JsonFieldType.BOOLEAN).description("북마크 여부"),
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
    @DisplayName("프로젝트를 생성한다.")
    void createProject() throws Exception {
        // given
        List<MemberRequest> memberRequest = List.of(
                new MemberRequest("학생 이름 1", Role.STUDENT),
                new MemberRequest("학생 이름 2", Role.STUDENT),
                new MemberRequest("교수 이름 1", Role.PROFESSOR)
        );

        ProjectRequest projectRequest = new ProjectRequest(1L, 2L, "프로젝트 이름", ProjectType.STARTUP, ProjectCategory.BIG_DATA_ANALYSIS, "팀 이름", "유튜브 ID", "파이썬, SQL", 2021, AwardStatus.NONE, memberRequest);

        ProjectDetailResponse response = new ProjectDetailResponse(
                1L,
                new FileResponse(
                        2L,
                        "썸네일 uuid",
                        "썸네일 파일 이름",
                        "썸네일 mime 타입"
                ),
                new FileResponse(
                        2L,
                        "포스터 uuid",
                        "포트서 파일 이름",
                        "포스터 mime 타입"
                ),
                "프로젝트 이름",
                ProjectType.STARTUP,
                ProjectCategory.BIG_DATA_ANALYSIS,
                "팀 이름",
                "유튜브 ID",
                List.of("파이썬", "SQL"),
                2024,
                AwardStatus.FIRST,
                List.of("학생 이름 1", "학생 이름 2"),
                List.of("교수 이름 1"),
                0,
                false,
                false,
                List.of(
                        new CommentResponse(1L, 1L, "유저 이름", true, "댓글 내용", LocalDateTime.now(), LocalDateTime.now()),
                        new CommentResponse(2L, 1L, "유저 이름", false, "댓글 내용", LocalDateTime.now(), LocalDateTime.now()),
                        new CommentResponse(3L, 1L, "유저 이름", false, "댓글 내용", LocalDateTime.now(), LocalDateTime.now())
                )
        );

        when(projectService.createProject(any(ProjectRequest.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                post("/projects")
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                        .content(objectMapper.writeValueAsString(projectRequest))
        );

        // then
        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("thumbnailId").type(JsonFieldType.NUMBER).description("썸네일 ID"),
                                fieldWithPath("posterId").type(JsonFieldType.NUMBER).description("포스터 ID"),
                                fieldWithPath("projectName").type(JsonFieldType.STRING).description("프로젝트 이름"),
                                fieldWithPath("projectType").type(JsonFieldType.STRING).description("프로젝트 타입: RESEARCH_AND_BUSINESS_FOUNDATION, LAB, STARTUP, CLUB"),
                                fieldWithPath("projectCategory").type(JsonFieldType.STRING).description("프로젝트 카테고리: COMPUTER_VISION, SYSTEM_NETWORK, WEB_APPLICATION, SECURITY_SOFTWARE_ENGINEERING, NATURAL_LANGUAGE_PROCESSING, BIG_DATA_ANALYSIS, AI_MACHINE_LEARNING, INTERACTION_AUGMENTED_REALITY\n"),
                                fieldWithPath("teamName").type(JsonFieldType.STRING).description("팀 이름"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("프로젝트 youtubeId"),
                                fieldWithPath("techStack").type(JsonFieldType.STRING).description("기술 스택"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("프로젝트 년도"),
                                fieldWithPath("awardStatus").type(JsonFieldType.STRING).description("수상 여부: NONE, FIRST, SECOND, THIRD, FOURTH, FIFTH"),
                                fieldWithPath("members").type(JsonFieldType.OBJECT).description("멤버"),
                                fieldWithPath("members[].name").type(JsonFieldType.STRING).description("멤버 이름"),
                                fieldWithPath("members[].role").type(JsonFieldType.STRING).description("멤버 역할: STUDENT, PROFESSOR")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("프로젝트 ID"),
                                fieldWithPath("thumbnailInfo").type(JsonFieldType.OBJECT).description("썸네일 정보"),
                                fieldWithPath("thumbnailInfo.id").type(JsonFieldType.NUMBER).description("썸네일 ID"),
                                fieldWithPath("thumbnailInfo.uuid").type(JsonFieldType.STRING).description("썸네일 UUID"),
                                fieldWithPath("thumbnailInfo.name").type(JsonFieldType.STRING).description("썸네일 파일 이름"),
                                fieldWithPath("thumbnailInfo.mimeType").type(JsonFieldType.STRING).description("썸네일 MIME 타입"),
                                fieldWithPath("posterInfo").type(JsonFieldType.OBJECT).description("포스터 정보"),
                                fieldWithPath("posterInfo.id").type(JsonFieldType.NUMBER).description("포스터 ID"),
                                fieldWithPath("posterInfo.uuid").type(JsonFieldType.STRING).description("포스터 UUID"),
                                fieldWithPath("posterInfo.name").type(JsonFieldType.STRING).description("포스터 파일 이름"),
                                fieldWithPath("posterInfo.mimeType").type(JsonFieldType.STRING).description("포스터 MIME 타입"),
                                fieldWithPath("projectName").type(JsonFieldType.STRING).description("프로젝트 이름"),
                                fieldWithPath("projectType").type(JsonFieldType.STRING).description("프로젝트 타입: RESEARCH_AND_BUSINESS_FOUNDATION, LAB, STARTUP, CLUB"),
                                fieldWithPath("projectCategory").type(JsonFieldType.STRING).description("프로젝트 카테고리: COMPUTER_VISION, SYSTEM_NETWORK, WEB_APPLICATION, SECURITY_SOFTWARE_ENGINEERING, NATURAL_LANGUAGE_PROCESSING, BIG_DATA_ANALYSIS, AI_MACHINE_LEARNING, INTERACTION_AUGMENTED_REALITY"),
                                fieldWithPath("teamName").type(JsonFieldType.STRING).description("팀 이름"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("프로젝트 youtubeId"),
                                fieldWithPath("techStack").type(JsonFieldType.ARRAY).description("기술 스택"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("프로젝트 년도"),
                                fieldWithPath("awardStatus").type(JsonFieldType.STRING).description("수상 여부: NONE, FIRST, SECOND, THIRD, FOURTH, FIFTH "),
                                fieldWithPath("studentNames").type(JsonFieldType.ARRAY).description("학생 이름"),
                                fieldWithPath("professorNames").type(JsonFieldType.ARRAY).description("교수 이름"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("like").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
                                fieldWithPath("bookMark").type(JsonFieldType.BOOLEAN).description("북마크 여부"),
                                fieldWithPath("comments").type(JsonFieldType.OBJECT).description("댓글"),
                                fieldWithPath("comments[].id").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("comments[].projectId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("comments[].userName").type(JsonFieldType.STRING).description("유저 이름"),
                                fieldWithPath("comments[].isAnonymous").type(JsonFieldType.BOOLEAN).description("익명 여부"),
                                fieldWithPath("comments[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("comments[].createdAt").type(JsonFieldType.STRING).description("생성 시간"),
                                fieldWithPath("comments[].updatedAt").type(JsonFieldType.STRING).description("수정 시간")
                        )
                ));
    }

    @Test
    @DisplayName("상세 프로젝트 정보를 조회한다.")
    void getProject() throws Exception {
        // given
        ProjectDetailResponse response = new ProjectDetailResponse(
                1L,
                new FileResponse(
                        2L,
                        "썸네일 uuid",
                        "썸네일 파일 이름",
                        "썸네일 mime 타입"
                ),
                new FileResponse(
                        2L,
                        "포스터 uuid",
                        "포트서 파일 이름",
                        "포스터 mime 타입"
                ),
                "프로젝트 이름",
                ProjectType.STARTUP,
                ProjectCategory.BIG_DATA_ANALYSIS,
                "팀 이름",
                "유튜브 ID",
                List.of("파이썬", "SQL"),
                2024,
                AwardStatus.FIRST,
                List.of("학생 이름 1", "학생 이름 2"),
                List.of("교수 이름 1"),
                0,
                false,
                false,
                List.of(
                        new CommentResponse(1L, 1L, "유저 이름", true, "댓글 내용", LocalDateTime.now(), LocalDateTime.now()),
                        new CommentResponse(2L, 1L, "유저 이름", false, "댓글 내용", LocalDateTime.now(), LocalDateTime.now()),
                        new CommentResponse(3L, 1L, "유저 이름", false, "댓글 내용", LocalDateTime.now(), LocalDateTime.now())
                )
        );

        when(projectService.getProject(anyLong(), any(User.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                get("/projects/{projectId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ALL_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("프로젝트 ID"),
                                fieldWithPath("thumbnailInfo").type(JsonFieldType.OBJECT).description("썸네일 정보"),
                                fieldWithPath("thumbnailInfo.id").type(JsonFieldType.NUMBER).description("썸네일 ID"),
                                fieldWithPath("thumbnailInfo.uuid").type(JsonFieldType.STRING).description("썸네일 UUID"),
                                fieldWithPath("thumbnailInfo.name").type(JsonFieldType.STRING).description("썸네일 파일 이름"),
                                fieldWithPath("thumbnailInfo.mimeType").type(JsonFieldType.STRING).description("썸네일 MIME 타입"),
                                fieldWithPath("posterInfo").type(JsonFieldType.OBJECT).description("포스터 정보"),
                                fieldWithPath("posterInfo.id").type(JsonFieldType.NUMBER).description("포스터 ID"),
                                fieldWithPath("posterInfo.uuid").type(JsonFieldType.STRING).description("포스터 UUID"),
                                fieldWithPath("posterInfo.name").type(JsonFieldType.STRING).description("포스터 파일 이름"),
                                fieldWithPath("posterInfo.mimeType").type(JsonFieldType.STRING).description("포스터 MIME 타입"),
                                fieldWithPath("projectName").type(JsonFieldType.STRING).description("프로젝트 이름"),
                                fieldWithPath("projectType").type(JsonFieldType.STRING).description("프로젝트 타입: RESEARCH_AND_BUSINESS_FOUNDATION, LAB, STARTUP, CLUB"),
                                fieldWithPath("projectCategory").type(JsonFieldType.STRING).description("프로젝트 카테고리: COMPUTER_VISION, SYSTEM_NETWORK, WEB_APPLICATION, SECURITY_SOFTWARE_ENGINEERING, NATURAL_LANGUAGE_PROCESSING, BIG_DATA_ANALYSIS, AI_MACHINE_LEARNING, INTERACTION_AUGMENTED_REALITY"),
                                fieldWithPath("teamName").type(JsonFieldType.STRING).description("팀 이름"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("프로젝트 youtubeId"),
                                fieldWithPath("techStack").type(JsonFieldType.ARRAY).description("기술 스택"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("프로젝트 년도"),
                                fieldWithPath("awardStatus").type(JsonFieldType.STRING).description("수상 여부: NONE, FIRST, SECOND, THIRD, FOURTH, FIFTH "),
                                fieldWithPath("studentNames").type(JsonFieldType.ARRAY).description("학생 이름"),
                                fieldWithPath("professorNames").type(JsonFieldType.ARRAY).description("교수 이름"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("like").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
                                fieldWithPath("bookMark").type(JsonFieldType.BOOLEAN).description("북마크 여부"),
                                fieldWithPath("comments").type(JsonFieldType.OBJECT).description("댓글"),
                                fieldWithPath("comments[].id").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("comments[].projectId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("comments[].userName").type(JsonFieldType.STRING).description("유저 이름"),
                                fieldWithPath("comments[].isAnonymous").type(JsonFieldType.BOOLEAN).description("익명 여부"),
                                fieldWithPath("comments[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("comments[].createdAt").type(JsonFieldType.STRING).description("생성 시간"),
                                fieldWithPath("comments[].updatedAt").type(JsonFieldType.STRING).description("수정 시간")
                        )
                ));
    }

    @Test
    @DisplayName("상세 프로젝트 정보를 수정한다.")
    void updateProject() throws Exception {
        // given
        List<MemberRequest> memberRequest = List.of(
                new MemberRequest("학생 이름 3", Role.STUDENT),
                new MemberRequest("학생 이름 4", Role.STUDENT),
                new MemberRequest("교수 이름 2", Role.PROFESSOR)
        );

        ProjectRequest projectRequest = new ProjectRequest(
                3L,
                4L,
                "프로젝트 이름",
                ProjectType.LAB,
                ProjectCategory.COMPUTER_VISION,
                "팀 이름",
                "유튜브 ID",
                "파이썬, OpenCV, diffusers",
                2024,
                AwardStatus.FIRST,
                memberRequest
        );

        ProjectDetailResponse response = new ProjectDetailResponse(
                1L,
                new FileResponse(
                        3L,
                        "썸네일 uuid",
                        "썸네일 파일 이름",
                        "썸네일 mime 타입"
                ),
                new FileResponse(
                        4L,
                        "포스터 uuid",
                        "포트서 파일 이름",
                        "포스터 mime 타입"
                ),
                "프로젝트 이름",
                ProjectType.LAB,
                ProjectCategory.COMPUTER_VISION,
                "팀 이름",
                "유튜브 ID",
                List.of("파이썬", "OpenCV", "diffusers"),
                2024,
                AwardStatus.FIRST,
                List.of("학생 이름 3", "학생 이름 4"),
                List.of("교수 이름 2"),
                100,
                false,
                false,
                List.of(
                        new CommentResponse(1L, 1L, "유저 이름", true, "댓글 내용", LocalDateTime.now(), LocalDateTime.now()),
                        new CommentResponse(2L, 1L, "유저 이름", false, "댓글 내용", LocalDateTime.now(), LocalDateTime.now()),
                        new CommentResponse(3L, 1L, "유저 이름", false, "댓글 내용", LocalDateTime.now(), LocalDateTime.now())
                )
        );

        when(projectService.updateProject(anyLong(), any(ProjectRequest.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                put("/projects/{projectId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                        .content(objectMapper.writeValueAsString(projectRequest))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 ID")
                        ),
                        requestFields(
                                fieldWithPath("thumbnailId").type(JsonFieldType.NUMBER).description("썸네일 ID"),
                                fieldWithPath("posterId").type(JsonFieldType.NUMBER).description("포스터 ID"),
                                fieldWithPath("projectName").type(JsonFieldType.STRING).description("프로젝트 이름"),
                                fieldWithPath("projectType").type(JsonFieldType.STRING).description("프로젝트 타입: RESEARCH_AND_BUSINESS_FOUNDATION, LAB, STARTUP, CLUB"),
                                fieldWithPath("projectCategory").type(JsonFieldType.STRING).description("프로젝트 카테고리: COMPUTER_VISION, SYSTEM_NETWORK, WEB_APPLICATION, SECURITY_SOFTWARE_ENGINEERING, NATURAL_LANGUAGE_PROCESSING, BIG_DATA_ANALYSIS, AI_MACHINE_LEARNING, INTERACTION_AUGMENTED_REALITY\n"),
                                fieldWithPath("teamName").type(JsonFieldType.STRING).description("팀 이름"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("프로젝트 youtubeId"),
                                fieldWithPath("techStack").type(JsonFieldType.STRING).description("기술 스택"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("프로젝트 년도"),
                                fieldWithPath("awardStatus").type(JsonFieldType.STRING).description("수상 여부: NONE, FIRST, SECOND, THIRD, FOURTH, FIFTH"),
                                fieldWithPath("members").type(JsonFieldType.OBJECT).description("멤버"),
                                fieldWithPath("members[].name").type(JsonFieldType.STRING).description("멤버 이름"),
                                fieldWithPath("members[].role").type(JsonFieldType.STRING).description("멤버 역할: STUDENT, PROFESSOR")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("프로젝트 ID"),
                                fieldWithPath("thumbnailInfo").type(JsonFieldType.OBJECT).description("썸네일 정보"),
                                fieldWithPath("thumbnailInfo.id").type(JsonFieldType.NUMBER).description("썸네일 ID"),
                                fieldWithPath("thumbnailInfo.uuid").type(JsonFieldType.STRING).description("썸네일 UUID"),
                                fieldWithPath("thumbnailInfo.name").type(JsonFieldType.STRING).description("썸네일 파일 이름"),
                                fieldWithPath("thumbnailInfo.mimeType").type(JsonFieldType.STRING).description("썸네일 MIME 타입"),
                                fieldWithPath("posterInfo").type(JsonFieldType.OBJECT).description("포스터 정보"),
                                fieldWithPath("posterInfo.id").type(JsonFieldType.NUMBER).description("포스터 ID"),
                                fieldWithPath("posterInfo.uuid").type(JsonFieldType.STRING).description("포스터 UUID"),
                                fieldWithPath("posterInfo.name").type(JsonFieldType.STRING).description("포스터 파일 이름"),
                                fieldWithPath("posterInfo.mimeType").type(JsonFieldType.STRING).description("포스터 MIME 타입"),
                                fieldWithPath("projectName").type(JsonFieldType.STRING).description("프로젝트 이름"),
                                fieldWithPath("projectType").type(JsonFieldType.STRING).description("프로젝트 타입: RESEARCH_AND_BUSINESS_FOUNDATION, LAB, STARTUP, CLUB"),
                                fieldWithPath("projectCategory").type(JsonFieldType.STRING).description("프로젝트 카테고리: COMPUTER_VISION, SYSTEM_NETWORK, WEB_APPLICATION, SECURITY_SOFTWARE_ENGINEERING, NATURAL_LANGUAGE_PROCESSING, BIG_DATA_ANALYSIS, AI_MACHINE_LEARNING, INTERACTION_AUGMENTED_REALITY"),
                                fieldWithPath("teamName").type(JsonFieldType.STRING).description("팀 이름"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("프로젝트 youtubeId"),
                                fieldWithPath("techStack").type(JsonFieldType.ARRAY).description("기술 스택"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("프로젝트 년도"),
                                fieldWithPath("awardStatus").type(JsonFieldType.STRING).description("수상 여부: NONE, FIRST, SECOND, THIRD, FOURTH, FIFTH "),
                                fieldWithPath("studentNames").type(JsonFieldType.ARRAY).description("학생 이름"),
                                fieldWithPath("professorNames").type(JsonFieldType.ARRAY).description("교수 이름"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("like").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
                                fieldWithPath("bookMark").type(JsonFieldType.BOOLEAN).description("북마크 여부"),
                                fieldWithPath("comments").type(JsonFieldType.OBJECT).description("댓글"),
                                fieldWithPath("comments[].id").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("comments[].projectId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("comments[].userName").type(JsonFieldType.STRING).description("유저 이름"),
                                fieldWithPath("comments[].isAnonymous").type(JsonFieldType.BOOLEAN).description("익명 여부"),
                                fieldWithPath("comments[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("comments[].createdAt").type(JsonFieldType.STRING).description("생성 시간"),
                                fieldWithPath("comments[].updatedAt").type(JsonFieldType.STRING).description("수정 시간")
                        )
                ));
    }

    @Test
    @DisplayName("프로젝트를 삭제한다.")
    void deleteProject() throws Exception {
        // given
        doNothing().when(projectService).deleteProject(anyLong());

        // when
        ResultActions result = mockMvc.perform(
                delete("/projects/{projectId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
        result.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 ID")
                        )
                ));
    }

    @Test
    @DisplayName("관심 프로젝트를 추가한다.")
    void createProjectFavorite() throws Exception {
        // given
        doNothing().when(projectService).createProjectFavorite(anyLong(), any(User.class));

        // when
        ResultActions result = mockMvc.perform(
                post("/projects/{projectId}/favorite", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ALL_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
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
                                parameterWithName("projectId").description("프로젝트 ID")
                        )
                ));
    }

    @Test
    @DisplayName("관심 프로젝트를 삭제한다.")
    void deleteProjectFavorite() throws Exception {
        // given
        doNothing().when(projectService).deleteProjectFavorite(anyLong(), any(User.class));

        // when
        ResultActions result = mockMvc.perform(
                delete("/projects/{projectId}/favorite", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ALL_ACCESS_TOKEN)
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
                                parameterWithName("projectId").description("프로젝트 ID")
                        )
                ));
    }

    @Test
    @DisplayName("프로젝트 좋아요를 누른다.")
    void createProjectLike() throws Exception {
        // given
        doNothing().when(projectService).createProjectLike(anyLong(), any(User.class));

        // when
        ResultActions result = mockMvc.perform(
                post("/projects/{projectId}/like", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ALL_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
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
                                parameterWithName("projectId").description("프로젝트 ID")
                        )
                ));
    }

    @Test
    @DisplayName("프로젝트 좋아요를 취소한다.")
    void deleteProjectLike() throws Exception {
        // given
        doNothing().when(projectService).deleteProjectLike(anyLong(), any(User.class));

        // when
        ResultActions result = mockMvc.perform(
                delete("/projects/{projectId}/like", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ALL_ACCESS_TOKEN)
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
                                parameterWithName("projectId").description("프로젝트 ID")
                        )
                ));
    }

    @Test
    @DisplayName("프로젝트 댓글을 생성한다.")
    void createProjectComment() throws Exception {
        // given
        CommentRequest commentRequest = new CommentRequest("댓글 내용", true);

        CommentResponse commentResponse = new CommentResponse(1L,1L, "유저 이름", true,"댓글 내용" , LocalDateTime.now(), LocalDateTime.now());

        when(projectService.createProjectComment(anyLong(), any(User.class), any(CommentRequest.class))).thenReturn(commentResponse);

        // when
        ResultActions result = mockMvc.perform(
                post("/projects/{projectId}/comment", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ALL_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                        .content(objectMapper.writeValueAsString(commentRequest))
        );

        verify(projectService).createProjectComment(
                anyLong(), // 첫 번째 인자 (프로젝트 ID)
                any(User.class), // 두 번째 인자 (유저 ID)
                any(CommentRequest.class) // 세 번째 인자 (댓글 요청 객체)
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
                                parameterWithName("projectId").description("프로젝트 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("isAnonymous").type(JsonFieldType.BOOLEAN).description("익명 여부")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("댓글 ID"),
                                fieldWithPath("projectId").type(JsonFieldType.NUMBER).description("프로젝트 ID"),
                                fieldWithPath("userName").type(JsonFieldType.STRING).description("유저 이름"),
                                fieldWithPath("isAnonymous").type(JsonFieldType.BOOLEAN).description("익명 여부"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시간"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("수정 시간")
                        )

                ));


    }

    @Test
    @DisplayName("프로젝트 댓글을 삭제한다.")
    void deleteProjectComment() throws Exception {
        // given
        doNothing().when(projectService).deleteProjectComment(anyLong(), anyLong(), any(User.class));

        // when
        ResultActions result = mockMvc.perform(
                delete("/projects/{projectId}/comment/{commentId}", 1L, 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ALL_ACCESS_TOKEN)
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
                                parameterWithName("projectId").description("프로젝트 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        )
                ));
    }
}