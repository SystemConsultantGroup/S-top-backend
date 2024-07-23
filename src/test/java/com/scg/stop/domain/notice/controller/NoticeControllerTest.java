package com.scg.stop.domain.notice.controller;

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
import java.time.LocalDateTime;

import com.scg.stop.domain.notice.dto.request.NoticeRequestDto;
import com.scg.stop.domain.notice.dto.response.NoticeResponseDto;
import com.scg.stop.domain.notice.service.NoticeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(NoticeController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class NoticeControllerTest extends AbstractControllerTest {

    @MockBean
    private NoticeService noticeService;

    @Autowired
    private ObjectMapper objectMapper;

    // TODO: Auth 설정 추가
    // TODO: handle attached files
    @Test
    @DisplayName("공지 사항을 생성할 수 있다.")
    void createNotice() throws Exception {

        // given
        NoticeRequestDto request = new NoticeRequestDto("title", "content", true);
        NoticeResponseDto response = new NoticeResponseDto(1L, "title", "content", 0, true, LocalDateTime.now(), LocalDateTime.now());


        when(noticeService.createNotice(any())).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                post("/notices")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );


        // then
        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("공지 사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("공지 사항 내용"),
                                fieldWithPath("fixed").type(JsonFieldType.BOOLEAN).description("공지 사항 고정 여부")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("공지 사항 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("공지 사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("공지 사항 내용"),
                                fieldWithPath("hitCount").type(JsonFieldType.NUMBER).description("공지 사항 조회수"),
                                fieldWithPath("fixed").type(JsonFieldType.BOOLEAN).description("공지 사항 고정 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("공지 사항 생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("공지 사항 수정일")
                        )
                ));
    }

    @DisplayName("공지 사항 제목을 입력하지 않으면 예외가 발생한다.")
    @Test
    void createNoticeWithInvalidTitle() throws Exception {

        // given
        NoticeRequestDto request = new NoticeRequestDto("", "content", true);

        // when
        ResultActions result = mockMvc.perform(
                post("/notices")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isBadRequest());
    }

    @DisplayName("공지 사항 내용을 입력하지 않으면 예외가 발생한다.")
    @Test
    void createNoticeWithInvalidContent() throws Exception {

        // given
        NoticeRequestDto request = new NoticeRequestDto("title", "", true);

        // when
        ResultActions result = mockMvc.perform(
                post("/notices")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isBadRequest());
    }

    // TODO: fixed field 의 경우 default 가 false 이므로 비어있는 경우를 상정?
}