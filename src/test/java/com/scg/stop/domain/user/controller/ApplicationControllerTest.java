package com.scg.stop.domain.user.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.domain.user.domain.UserType;
import com.scg.stop.domain.user.dto.response.ApplicationListResponse;
import com.scg.stop.domain.user.service.ApplicationService;
import java.time.LocalDateTime;
import java.util.List;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ApplicationController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class ApplicationControllerTest extends AbstractControllerTest {

    @MockBean
    private ApplicationService applicationService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    // TODO Auth 설정 추가
    @Test
    @DisplayName("교수/기업관계자의 가입 신청 정보 리스트를 조회할 수 있다.")
    void getApplications() throws Exception  {

        // given
        ApplicationListResponse response1 = new ApplicationListResponse(1L, "김영한", "배민", null, UserType.INACTIVE_COMPANY, LocalDateTime.now(),
                LocalDateTime.now());
        ApplicationListResponse response2 = new ApplicationListResponse(2L, "김교수", "솦융대", "교수", UserType.INACTIVE_PROFESSOR, LocalDateTime.now(),
                LocalDateTime.now());
        ApplicationListResponse response3 = new ApplicationListResponse(3L, "박교수", "정통대", "교수", UserType.INACTIVE_PROFESSOR, LocalDateTime.now(),
                LocalDateTime.now());
        Page<ApplicationListResponse> page = new PageImpl<>(List.of(response1, response2, response3), PageRequest.of(0, 10), 3);

        when(applicationService.getApplications(any())).thenReturn(page);

        // when
        ResultActions result = mockMvc.perform(
                get("/applications").contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 [default: 0]").optional(),
                                parameterWithName("size").description("페이지 크기 [default: 10]").optional()
                        ),
                        responseFields(
                                // page
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
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("가입 신청 ID"),
                                fieldWithPath("content[].name").type(JsonFieldType.STRING).description("가입 신청자 이름"),
                                fieldWithPath("content[].division").type(JsonFieldType.STRING).description("소속").optional(),
                                fieldWithPath("content[].position").type(JsonFieldType.STRING).description("직책").optional(),
                                fieldWithPath("content[].userType").type(JsonFieldType.STRING).description("회원 유형 [INACTIVE_PROFESSOR, INACTIVE_COMPANY]"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING).description("가입 신청 정보 생성일"),
                                fieldWithPath("content[].updatedAt").type(JsonFieldType.STRING).description("가입 신청 정보 수정일")
                        )
                ));
    }
}