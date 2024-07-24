package com.scg.stop.domain.event.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.domain.event.dto.EventPeriodRequest;
import com.scg.stop.domain.event.dto.EventPeriodResponse;
import com.scg.stop.domain.event.service.EventPeriodService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(EventPeriodController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class EventPeriodControllerTest extends AbstractControllerTest {

    @MockBean
    private EventPeriodService eventPeriodService;

    @Autowired
    private ObjectMapper objectMapper;

    // TODO Auth 설정 추가
    @Test
    @DisplayName("이벤트 기간을 생성할 수 있다.")
    void createEventPeriod() throws Exception {

        // given
        EventPeriodRequest request = new EventPeriodRequest(2024, LocalDateTime.now(), LocalDateTime.now().plusDays(10));
        EventPeriodResponse response = new EventPeriodResponse(1L, 2024, LocalDateTime.now(), LocalDateTime.now().plusDays(10), LocalDateTime.now(), LocalDateTime.now());

        when(eventPeriodService.createEventPeriod(any(EventPeriodRequest.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                post("/eventPeriods")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("이벤트 연도"),
                                fieldWithPath("start").type(JsonFieldType.STRING).description("이벤트 시작 일시"),
                                fieldWithPath("end").type(JsonFieldType.STRING).description("이벤트 종료 일시")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("행사 기간 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("이벤트 연도"),
                                fieldWithPath("start").type(JsonFieldType.STRING).description("이벤트 시작 일시"),
                                fieldWithPath("end").type(JsonFieldType.STRING).description("이벤트 종료 일시"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("변경일")
                        )
                ));
    }

    @DisplayName("이벤트 연도를 입력하지 않으면 예외가 발생한다.")
    @Test
    void createEventPeriodWithInvalidYear() throws Exception {

        // given
        EventPeriodRequest request = new EventPeriodRequest( null, LocalDateTime.now(), LocalDateTime.now().plusDays(10));

        // when
        ResultActions result = mockMvc.perform(
                post("/eventPeriods")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isBadRequest());
    }
}