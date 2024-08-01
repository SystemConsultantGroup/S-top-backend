package com.scg.stop.domain.project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
import com.scg.stop.domain.project.dto.request.MemberRequest;
import com.scg.stop.domain.project.dto.request.ProjectRequest;
import com.scg.stop.domain.project.dto.response.ProjectDetailResponse;
import com.scg.stop.domain.project.dto.response.ProjectResponse;
import com.scg.stop.domain.project.service.ProjectService;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

@WebMvcTest(ProjectController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class ProjectControllerTest extends AbstractControllerTest {

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    // ToDo: getProjects() 코드가 base 브랜치에 있어서, 추후에 새로운 브랜치 만들어서 작성하기 지금 시작..!!!!!!!! 오밤...
    @DisplayName("간단한 프로젝트 리스트를 조회한다. | 페이지네이션")
    @Test
    void getProjects() throws Exception {
        // given
        ProjectResponse projectResponse1 = new ProjectResponse(1L, null, "프로젝트 이름 1", "팀 이름 2", List.of("테스트 학생 이름 1", "테스트 학생 이름 2"), List.of("테스트 교수 이름 1"), ProjectType.STARTUP, ProjectCategory.BIG_DATA_ANALYSIS, List.of("자바", "스프링"), 100, false);
        ProjectResponse projectResponse2 = new ProjectResponse(2L,null, "프로젝트 이름 1", "팀 이름 2", List.of("테스트 학생 이름 1", "테스트 학생 이름 2"), List.of("테스트 교수 이름 1"), ProjectType.STARTUP, ProjectCategory.BIG_DATA_ANALYSIS, List.of("자바", "스프링"), 100, false);
        Page<ProjectResponse> pageResponse = new PageImpl<>(List.of(projectResponse1, projectResponse2), PageRequest.of(0, 10), 2);

        when(projectService.getProjects(any(), any(), any(), any())).thenReturn(pageResponse);

        // when
        ResultActions result = mockMvc.perform(
                get("/projects")
                        .contentType(APPLICATION_JSON)
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
                                fieldWithPath("content[].id").description("프로젝트 ID"),
                                fieldWithPath("content[].thumbnailInfo").description("썸네일 정보"),
                                fieldWithPath("content[].projectName").description("프로젝트 이름"),
                                fieldWithPath("content[].teamName").description("팀 이름"),
                                fieldWithPath("content[].studentNames[]").description("학생 이름"),
                                fieldWithPath("content[].professorNames[]").description("교수 이름"),
                                fieldWithPath("content[].projectType").description("프로젝트 타입"),
                                fieldWithPath("content[].projectCategory").description("프로젝트 카테고리"),
                                fieldWithPath("content[].techStack").description("기술 스택"),
                                fieldWithPath("content[].likeCount").description("좋아요 수"),
                                fieldWithPath("content[].bookMark").description("북마크 여부"),
                                fieldWithPath("pageable").description("페이지 정보"),
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
                new MemberRequest("테스트 학생 이름 1", Role.STUDENT),
                new MemberRequest("테스트 학생 이름 2", Role.STUDENT),
                new MemberRequest("테스트 교수 이름 1", Role.PROFESSOR)
        );

        ProjectRequest projectRequest = new ProjectRequest(1L, 2L, "테스트 프로젝트 이름", ProjectType.STARTUP, ProjectCategory.BIG_DATA_ANALYSIS, "테스트 팀 이름", "테스트 유튜브 ID", "테스트, 기술, 스택", 2021, AwardStatus.NONE, memberRequest);

        ProjectDetailResponse response = new ProjectDetailResponse(1L, null, null, "테스트 프로젝트 이름", ProjectType.STARTUP, ProjectCategory.BIG_DATA_ANALYSIS, "테스트 팀 이름", "테스트 유튜브 ID", List.of("테스트", "기술", "스택"), 2021, AwardStatus.NONE, List.of("테스트 학생 이름 1", "테스트 학생 이름 2"), List.of("테스트 교수 이름 1"), 0, false);

        when(projectService.createProject(any(ProjectRequest.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                post("/projects")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequest))
        );

        // then
        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("thumbnailId").type(JsonFieldType.NUMBER).description("썸네일 ID"),
                                fieldWithPath("posterId").description("포스터 ID"),
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("projectType").description("프로젝트 타입"),
                                fieldWithPath("projectCategory").description("프로젝트 카테고리"),
                                fieldWithPath("teamName").description("팀 이름"),
                                fieldWithPath("youtubeId").description("프로젝트 yotubeId"),
                                fieldWithPath("techStack").description("기술 스택"),
                                fieldWithPath("year").description("프로젝트 년도"),
                                fieldWithPath("awardStatus").description("수상 여부"),
                                fieldWithPath("members").description("멤버"),
                                fieldWithPath("members[].name").description("멤버 이름"),
                                fieldWithPath("members[].role").description("멤버 역할")
                        ),
                        responseFields(
                                fieldWithPath("id").description("프로젝트 ID"),
                                fieldWithPath("thumbnailInfo").description("썸네일 정보"),
                                fieldWithPath("posterInfo").description("포스터 정보"),
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("projectType").description("프로젝트 타입"),
                                fieldWithPath("projectCategory").description("프로젝트 카테고리"),
                                fieldWithPath("teamName").description("팀 이름"),
                                fieldWithPath("youtubeId").description("프로젝트 yotubeId"),
                                fieldWithPath("techStack").description("기술 스택"),
                                fieldWithPath("year").description("프로젝트 년도"),
                                fieldWithPath("awardStatus").description("수상 여부"),
                                fieldWithPath("sutudentNames").description("학생 이름"),
                                fieldWithPath("professorNames").description("교수 이름"),
                                fieldWithPath("likeCount").description("좋아요 수"),
                                fieldWithPath("bookMark").description("북마크 여부")
                        )
                ));
    }

    @Test
    @DisplayName("상세 프로젝트 정보를 조회한다.")
    void getProject() throws Exception {
        // given
        ProjectDetailResponse response = new ProjectDetailResponse(1L, null, null, "테스트 프로젝트 이름", ProjectType.STARTUP, ProjectCategory.BIG_DATA_ANALYSIS, "테스트 팀 이름", "테스트 유튜브 ID", List.of("테스트", "기술", "스택"), 2021, AwardStatus.NONE, List.of("테스트 학생 이름 1", "테스트 학생 이름 2"), List.of("테스트 교수 이름 1"), 0, false);

        when(projectService.getProject(anyLong())).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                get("/projects/{projectId}", 1L)
                        .contentType(APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("프로젝트 ID"),
                                fieldWithPath("thumbnailInfo").description("썸네일 정보"),
                                fieldWithPath("posterInfo").description("포스터 정보"),
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("projectType").description("프로젝트 타입"),
                                fieldWithPath("projectCategory").description("프로젝트 카테고리"),
                                fieldWithPath("teamName").description("팀 이름"),
                                fieldWithPath("youtubeId").description("프로젝트 yotubeId"),
                                fieldWithPath("techStack").description("기술 스택"),
                                fieldWithPath("year").description("프로젝트 년도"),
                                fieldWithPath("awardStatus").description("수상 여부"),
                                fieldWithPath("sutudentNames").description("학생 이름"),
                                fieldWithPath("professorNames").description("교수 이름"),
                                fieldWithPath("likeCount").description("좋아요 수"),
                                fieldWithPath("bookMark").description("북마크 여부")
                        )
                ));
    }

    @Test
    @DisplayName("상세 프로젝트 정보를 수정한다.")
    void updateProject() throws Exception {
        // given
        List<MemberRequest> memberRequest = List.of(
                new MemberRequest("테스트 학생 이름 1", Role.STUDENT),
                new MemberRequest("테스트 학생 이름 2", Role.STUDENT),
                new MemberRequest("테스트 교수 이름 1", Role.PROFESSOR)
        );

        ProjectRequest projectRequest = new ProjectRequest(1L, 2L, "테스트 프로젝트 이름", ProjectType.STARTUP, ProjectCategory.BIG_DATA_ANALYSIS, "테스트 팀 이름", "테스트 유튜브 ID", "테스트, 기술, 스택", 2021, AwardStatus.NONE, memberRequest);

        ProjectDetailResponse response = new ProjectDetailResponse(1L, null, null, "테스트 프로젝트 이름", ProjectType.STARTUP, ProjectCategory.BIG_DATA_ANALYSIS, "테스트 팀 이름", "테스트 유튜브 ID", List.of("테스트", "기술", "스택"), 2021, AwardStatus.NONE, List.of("테스트 학생 이름 1", "테스트 학생 이름 2"), List.of("테스트 교수 이름 1"), 0, false);

        when(projectService.updateProject(anyLong(), any(ProjectRequest.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                put("/projects/{projectId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequest))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("thumbnailId").description("썸네일 ID"),
                                fieldWithPath("posterId").description("포스터 ID"),
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("projectType").description("프로젝트 타입"),
                                fieldWithPath("projectCategory").description("프로젝트 카테고리"),
                                fieldWithPath("teamName").description("팀 이름"),
                                fieldWithPath("youtubeId").description("프로젝트 yotubeId"),
                                fieldWithPath("techStack").description("기술 스택"),
                                fieldWithPath("year").description("프로젝트 년도"),
                                fieldWithPath("awardStatus").description("수상 여부"),
                                fieldWithPath("members").description("멤버"),
                                fieldWithPath("members[].name").description("멤버 이름"),
                                fieldWithPath("members[].role").description("멤버 역할")
                        ),
                        responseFields(
                                fieldWithPath("id").description("프로젝트 ID"),
                                fieldWithPath("thumbnailInfo").description("썸네일 정보"),
                                fieldWithPath("posterInfo").description("포스터 정보"),
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("projectType").description("프로젝트 타입"),
                                fieldWithPath("projectCategory").description("프로젝트 카테고리"),
                                fieldWithPath("teamName").description("팀 이름"),
                                fieldWithPath("youtubeId").description("프로젝트 yotubeId"),
                                fieldWithPath("techStack").description("기술 스택"),
                                fieldWithPath("year").description("프로젝트 년도"),
                                fieldWithPath("awardStatus").description("수상 여부"),
                                fieldWithPath("sutudentNames").description("학생 이름"),
                                fieldWithPath("professorNames").description("교수 이름"),
                                fieldWithPath("likeCount").description("좋아요 수"),
                                fieldWithPath("bookMark").description("북마크 여부")
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
        );

        // then
        result.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 ID")
                        )
                ));
    }
}
