package com.scg.stop.aiHub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.aihub.controller.AiHubController;
import com.scg.stop.aihub.dto.request.AiHubDatasetRequest;
import com.scg.stop.aihub.dto.request.AiHubModelRequest;
import com.scg.stop.aihub.dto.response.AiHubDatasetResponse;
import com.scg.stop.aihub.dto.response.AiHubModelResponse;
import com.scg.stop.aihub.service.AiHubService;
import com.scg.stop.configuration.AbstractControllerTest;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AiHubController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class AiHubControllerTest extends AbstractControllerTest {

    @MockBean
    private AiHubService aiHubService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("AI Hub 모델 리스트를 조회할 수 있다.")
    void getAiHubModels() throws Exception {

        // given

        AiHubModelRequest aiHubModelRequest = new AiHubModelRequest(
                "title",
                Arrays.asList("학습 모델 1"),
                Arrays.asList("주제 1"),
                Arrays.asList(2024),
                "담당 교수 1",
                Arrays.asList("학생 1")
        );

        AiHubModelResponse aiHubModelResponse1 = new AiHubModelResponse(
                "page",
                "노션 object 아이디",
                "커버 이미지 link",
                "title", Arrays.asList("학습 모델 1"),
                Arrays.asList("주제 1"),
                Arrays.asList(2024),
                "담당 교수 1",
                Arrays.asList("학생 1", "학생 2"),
                "노션 redirect url");

        AiHubModelResponse aiHubModelResponse2 = new AiHubModelResponse(
                "page",
                "노션 object 아이디",
                "커버 이미지 link",
                "title", Arrays.asList("학습 모델 1"),
                Arrays.asList("주제 1", "주제 2"),
                Arrays.asList(2024),
                "담당 교수 1",
                Arrays.asList("학생 1", "학생 2", "학생 3"),
                "노션 redirect url");
        Page<AiHubModelResponse> page = new PageImpl<>(List.of(aiHubModelResponse1, aiHubModelResponse2), PageRequest.of(0, 10), 2);

        when(aiHubService.getAiHubModels(any(AiHubModelRequest.class), any(Pageable.class))).thenReturn(page);

        // when
        ResultActions result = mockMvc.perform(
                post("/aihub/models")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aiHubModelRequest))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 [default: 0]").optional(),
                                parameterWithName("size").description("페이지 크기 [default: 10]").optional()
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("AI 모델 제목"),
                                fieldWithPath("learningModels").type(JsonFieldType.ARRAY).description("학습 모델"),
                                fieldWithPath("topics").type(JsonFieldType.ARRAY).description("주제 분류"),
                                fieldWithPath("developmentYears").type(JsonFieldType.ARRAY).description("개발 년도"),
                                fieldWithPath("professor").type(JsonFieldType.STRING).description("담당 교수"),
                                fieldWithPath("participants").type(JsonFieldType.ARRAY).description("참여 학생")
                        ),
                        responseFields(
                                fieldWithPath("content[].object").type(JsonFieldType.STRING).description("object 종류"),
                                fieldWithPath("content[].id").type(JsonFieldType.STRING).description("노션 object 아이디"),
                                fieldWithPath("content[].cover").type(JsonFieldType.STRING).description("커버 이미지 link"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("AI 모델 제목"),
                                fieldWithPath("content[].learningModels").type(JsonFieldType.ARRAY).description("학습 모델"),
                                fieldWithPath("content[].topics").type(JsonFieldType.ARRAY).description("주제 분류"),
                                fieldWithPath("content[].developmentYears").type(JsonFieldType.ARRAY).description("개발 년도"),
                                fieldWithPath("content[].professor").type(JsonFieldType.STRING).description("담당 교수"),
                                fieldWithPath("content[].participants").type(JsonFieldType.ARRAY).description("참여 학생"),
                                fieldWithPath("content[].url").type(JsonFieldType.STRING).description("노션 redirect url"),
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
    @DisplayName("AI Hub 데이터셋 리스트를 조회할 수 있다.")
    void getAiHubDatasets() throws Exception {

        // given

        AiHubDatasetRequest aiHubDatasetRequest = new AiHubDatasetRequest(
                "title",
                Arrays.asList("주제 1"),
                Arrays.asList("데이터 유형 1"),
                Arrays.asList(2024),
                "담당 교수 1",
                Arrays.asList("학생 1")
        );

        AiHubDatasetResponse aiHubDatasetResponse1 = new AiHubDatasetResponse(
                "page",
                "노션 object 아이디",
                "커버 이미지 link",
                "title", Arrays.asList("주제 1"),
                Arrays.asList("데이터 유형 1"),
                Arrays.asList(2024),
                "담당 교수 1",
                Arrays.asList("학생 1", "학생 2"),
                "노션 redirect url");

        AiHubDatasetResponse aiHubDatasetResponse2 = new AiHubDatasetResponse(
                "page",
                "노션 object 아이디",
                "커버 이미지 link",
                "title", Arrays.asList("주제 1", "주제 2"),
                Arrays.asList("데이터 유형 1"),
                Arrays.asList(2024),
                "담당 교수 1",
                Arrays.asList("학생 1", "학생 2", "학생 3"),
                "노션 redirect url");
        Page<AiHubDatasetResponse> page = new PageImpl<>(List.of(aiHubDatasetResponse1, aiHubDatasetResponse2), PageRequest.of(0, 10), 2);

        when(aiHubService.getAiHubDatasets(any(AiHubDatasetRequest.class), any(Pageable.class))).thenReturn(page);

        // when
        ResultActions result = mockMvc.perform(
                post("/aihub/datasets")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aiHubDatasetRequest))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 [default: 0]").optional(),
                                parameterWithName("size").description("페이지 크기 [default: 10]").optional()
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("AI 데이터셋 제목"),
                                fieldWithPath("dataTypes").type(JsonFieldType.ARRAY).description("데이터 유형"),
                                fieldWithPath("topics").type(JsonFieldType.ARRAY).description("주제 분류"),
                                fieldWithPath("developmentYears").type(JsonFieldType.ARRAY).description("구축 년도"),
                                fieldWithPath("professor").type(JsonFieldType.STRING).description("담당 교수"),
                                fieldWithPath("participants").type(JsonFieldType.ARRAY).description("참여 학생")
                        ),
                        responseFields(
                                fieldWithPath("content[].object").type(JsonFieldType.STRING).description("object 종류"),
                                fieldWithPath("content[].id").type(JsonFieldType.STRING).description("노션 object 아이디"),
                                fieldWithPath("content[].cover").type(JsonFieldType.STRING).description("커버 이미지 link"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("AI 데이터셋 제목"),
                                fieldWithPath("content[].topics").type(JsonFieldType.ARRAY).description("주제 분류"),
                                fieldWithPath("content[].dataTypes").type(JsonFieldType.ARRAY).description("데이터 유형"),
                                fieldWithPath("content[].developmentYears").type(JsonFieldType.ARRAY).description("구축 년도"),
                                fieldWithPath("content[].professor").type(JsonFieldType.STRING).description("담당 교수"),
                                fieldWithPath("content[].participants").type(JsonFieldType.ARRAY).description("참여 학생"),
                                fieldWithPath("content[].url").type(JsonFieldType.STRING).description("노션 redirect url"),
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
}

