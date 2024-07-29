package com.scg.stop.domain.video.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.domain.video.domain.JobInterview;
import com.scg.stop.domain.video.domain.JobInterviewCategory;
import com.scg.stop.domain.video.dto.request.JobInterviewRequest;
import com.scg.stop.domain.video.dto.response.JobInterviewResponse;
import com.scg.stop.domain.video.repository.JobInterviewRepository;
import com.scg.stop.domain.video.service.JobInterviewService;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobInterviewController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class JobInterviewControllerTest extends AbstractControllerTest {
    @MockBean
    private JobInterviewService jobInterviewService;

    @Autowired
    private ObjectMapper objectMapper;

    // TODO: AuthUser 추가
    @Test
    @DisplayName("잡페어 인터뷰를 생성한다.")
    void createJobInterview() throws Exception {

        // given
        JobInterviewRequest request = new JobInterviewRequest("title", "5ndbqo4ngHs", 2023, JobInterviewCategory.INTERN);
        JobInterviewResponse response = new JobInterviewResponse(1L, "title", "5ndbqo4ngHs", 2023, JobInterviewCategory.INTERN, LocalDateTime.now(), LocalDateTime.now());

        when(jobInterviewService.createJobInterview(any())).thenReturn(response);
        // when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/jobInterviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then

        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("잡페어 인터뷰 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 연도"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("잡페어 인터뷰 카테고리: SENIOR, INTERN")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("잡페어 인터뷰 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 연도"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("잡페어 인터뷰 카테고리: SENIOR, INTERN"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("잡페어 인터뷰 생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("잡페어 인터뷰 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("잡페어 인터뷰 생성시 제목이 빠진 경우 예외가 발생한다.")
    void createJobInterviewWithInvalidTitle() throws Exception {
        // given
        JobInterviewRequest request = new JobInterviewRequest("", "5ndbqo4ngHs", 2023, JobInterviewCategory.INTERN);

        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/jobInterviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잡페어 인터뷰 생성시 enum에 없는 카테고리인 경우 예외가 발생한다.")
    void createJobInterviewWithInvalidCategory() throws Exception {
        //given
        JobInterviewRequest request = new JobInterviewRequest("title", "5ndbqo4ngHs", 2023, null);

        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/jobInterviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잡페어 인터뷰 리스트를 조회할 수 있다.")
    void getJobInterviewList() throws Exception {
        //given
        JobInterviewResponse interview1 = new JobInterviewResponse(1L, "타이틀1", "아이디1", 2023, JobInterviewCategory.INTERN, LocalDateTime.now(), LocalDateTime.now());
        JobInterviewResponse interview2 = new JobInterviewResponse(2L, "타이틀2", "아이디2", 2023, JobInterviewCategory.SENIOR, LocalDateTime.now(), LocalDateTime.now());
        Page<JobInterviewResponse> page = new PageImpl<>(List.of(interview1, interview2), PageRequest.of(0,10),2);

        when(jobInterviewService.getJobInterviews(any(), any(), any(), any())).thenReturn(page);

        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/jobInterviews").contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("year").description("찾고자 하는 잡페어 인터뷰 연도").optional(),
                                parameterWithName("category").description("찾고자 하는 잡페어 인터뷰 카테고리: SENIOR, INTERN").optional(),
                                parameterWithName("title").description("찾고자 하는 잡페어 인터뷰의 제목").optional(),
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
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 ID"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("잡페어 인터뷰 제목"),
                                fieldWithPath("content[].youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("content[].year").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 연도"),
                                fieldWithPath("content[].category").type(JsonFieldType.STRING).description("잡페어 인터뷰 카테고리: SENIOR, INTERN"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING).description("잡페어 인터뷰 생성일"),
                                fieldWithPath("content[].updatedAt").type(JsonFieldType.STRING).description("잡페어 인터뷰 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("잡페어 인터뷰 1개를 조회할 수 있다.")
    void getJobInterview() throws Exception {
        //given
        JobInterviewResponse response = new JobInterviewResponse(1L, "제목", "유튜브ID", 2023, JobInterviewCategory.INTERN, LocalDateTime.now(), LocalDateTime.now());

        when(jobInterviewService.getJobInterview(any())).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/jobInterviews/{jobInterviewId}", 1L).contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                           parameterWithName("jobInterviewId").description("조회할 잡페어 인터뷰의 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("잡페어 인터뷰 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 연도"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("잡페어 인터뷰 카테고리: SENIOR, INTERN"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("잡페어 인터뷰 생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("잡페어 인터뷰 수정일")
                        )
                ));

    }

    @Test
    @DisplayName("잡페어 인터뷰를 수정할 수 있다.")
    void updateJobInterview() throws Exception {
        //given
        JobInterviewRequest request = new JobInterviewRequest("수정된 제목", "수정된 유튜브 ID", 2021, JobInterviewCategory.INTERN);
        JobInterviewResponse response = new JobInterviewResponse(1L, "수정된 제목", "수정된 유튜브 ID", 2021, JobInterviewCategory.INTERN, LocalDateTime.of(2021,1,1,12,0), LocalDateTime.now());

        when(jobInterviewService.updateJobInterview(any(), any(JobInterviewRequest.class))).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.put("/jobInterviews/{jobInterviewId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("jobInterviewId").description("수정할 잡페어 인터뷰의 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("잡페어 인터뷰 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 연도"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("잡페어 인터뷰 카테고리: SENIOR, INTERN")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("잡페어 인터뷰 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 연도"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("잡페어 인터뷰 카테고리: SENIOR, INTERN"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("잡페어 인터뷰 생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("잡페어 인터뷰 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("잡페어 인터뷰를 삭제할 수 있다.")
    void deleteJobInterview() throws Exception {
        //given
        doNothing().when(jobInterviewService).deleteJobInterviewById(any());
        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/jobInterviews/{jobInterviewId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("jobInterviewId").description("삭제할 잡페어 인터뷰의 ID")
                        )
                ));
    }




}
