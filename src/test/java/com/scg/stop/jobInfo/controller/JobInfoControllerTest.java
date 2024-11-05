package com.scg.stop.jobInfo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.jobInfo.dto.request.JobInfoRequest;
import com.scg.stop.jobInfo.dto.response.JobInfoResponse;
import com.scg.stop.jobInfo.service.JobInfoService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobInfoController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class JobInfoControllerTest extends AbstractControllerTest {

    @MockBean
    private JobInfoService jobInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Job Info 리스트를 조회할 수 있다.")
    void getJobInfos() throws Exception {

        // given

        JobInfoRequest jobInfoRequest = new JobInfoRequest(
                "기업명 1",
                List.of("고용 형태 1"),
                "근무 지역 1",
                "채용 포지션 1",
                "채용 시점 1",
                List.of("open")
        );

        JobInfoResponse jobInfoResponse1 = new JobInfoResponse(
                "page",
                "노션 object 아이디 1",
                "기업명 1",
                List.of("고용 형태 1"),
                "근무 지역 1",
                "채용 포지션 1",
                "로고 url",
                "연봉",
                "웹사이트 url",
                List.of("open"),
                "채용 시점 1",
                "redirect url"

        );

        JobInfoResponse jobInfoResponse2 = new JobInfoResponse(
                "page",
                "노션 object 아이디 2",
                "기업명 1",
                List.of("고용 형태 1", "고용 형태 2"),
                "근무 지역 2",
                "채용 포지션 1, 채용 시점 2",
                "로고 url",
                "연봉",
                "웹사이트 url",
                List.of("open"),
                "채용 시점 1",
                "redirect url"
        );

        Page<JobInfoResponse> page = new PageImpl<>(List.of(jobInfoResponse1, jobInfoResponse2), PageRequest.of(0, 10), 2);

        when(jobInfoService.getJobInfos(any(JobInfoRequest.class), any(Pageable.class))).thenReturn(page);

        // when
        ResultActions result = mockMvc.perform(
                post("/jobInfos")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jobInfoRequest))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 [default: 0]").optional(),
                                parameterWithName("size").description("페이지 크기 [default: 10]").optional()
                        ),
                        requestFields(
                                fieldWithPath("company").type(JsonFieldType.STRING).description("기업명"),
                                fieldWithPath("jobTypes").type(JsonFieldType.ARRAY).description("고용 형태"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("근무 지역"),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("채용 포지션"),
                                fieldWithPath("hiringTime").type(JsonFieldType.STRING).description("채용 시점"),
                                fieldWithPath("state").type(JsonFieldType.ARRAY).description("채용 상태")
                        ),
                        responseFields(
                                fieldWithPath("content[].object").type(JsonFieldType.STRING).description("object 종류"),
                                fieldWithPath("content[].id").type(JsonFieldType.STRING).description("노션 object 아이디"),
                                fieldWithPath("content[].company").type(JsonFieldType.STRING).description("기업명"),
                                fieldWithPath("content[].jobTypes").type(JsonFieldType.ARRAY).description("고용 형태"),
                                fieldWithPath("content[].region").type(JsonFieldType.STRING).description("근무 지역"),
                                fieldWithPath("content[].position").type(JsonFieldType.STRING).description("채용 포지션"),
                                fieldWithPath("content[].logo").type(JsonFieldType.STRING).description("로고 url"),
                                fieldWithPath("content[].salary").type(JsonFieldType.STRING).description("연봉"),
                                fieldWithPath("content[].website").type(JsonFieldType.STRING).description("웹사이트 url"),
                                fieldWithPath("content[].state").type(JsonFieldType.ARRAY).description("채용 상태"),
                                fieldWithPath("content[].hiringTime").type(JsonFieldType.STRING).description("채용 시점"),
                                fieldWithPath("content[].url").type(JsonFieldType.STRING).description("redirect url"),
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
