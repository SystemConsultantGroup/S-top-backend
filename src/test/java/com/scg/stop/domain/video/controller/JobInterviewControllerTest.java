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
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                post("/jobInterviews")
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
                post("/jobInterviews")
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
                post("/jobInterviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        result.andExpect(status().isBadRequest());
    }





}
