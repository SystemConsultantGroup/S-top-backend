package com.scg.stop.domain.video.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.domain.video.dto.request.TalkRequest;
import com.scg.stop.domain.video.dto.response.TalkResponse;
import com.scg.stop.domain.video.service.TalkService;
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
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TalkController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class TalkControllerTest extends AbstractControllerTest {
    @MockBean
    private TalkService talkService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("대담영상을 생성한다.")
    void createTalk() throws Exception {

        //given
        TalkRequest request= new TalkRequest("title", "youtubeId", true, 2024);
        TalkResponse response = new TalkResponse(1L, "title", "youtubeId", 2024, true, LocalDateTime.now(), LocalDateTime.now());

        when(talkService.createTalk(any())).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(
                post("/talks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("대담 영상 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("대담 영상 연도"),
                                fieldWithPath("hasQuiz").type(JsonFieldType.BOOLEAN).description("퀴즈 여부(존재시 true)")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("대담 영상 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("대담 영상 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("대담 영상 연도"),
                                fieldWithPath("hasQuiz").type(JsonFieldType.BOOLEAN).description("퀴즈 여부(존재시 true)"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("대담 영상 생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("대담 영상 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("대담 영상 리스트를 조회할 수 있다.")
    void getTalkList() throws Exception {
        //given
        TalkResponse response1 = new TalkResponse(1L, "title1", "youtubeId", 2024, true, LocalDateTime.now(), LocalDateTime.now());
        TalkResponse response2 = new TalkResponse(2L, "title2", "youtubeId2", 2023, false, LocalDateTime.now(), LocalDateTime.now());
        Page<TalkResponse> page = new PageImpl<>(List.of(response1,response2), PageRequest.of(0,10),2);

        when(talkService.getTalks(any(), any(), any())).thenReturn(page);

        //when
        ResultActions result = mockMvc.perform(
                get("/talks")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("year").description("찾고자 하는 대담 영상의 연도").optional(),
                                parameterWithName("title").description("찾고자 하는 대담 영상의 제목 일부").optional(),
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
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("대담 영상 ID"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("대담 영상 제목"),
                                fieldWithPath("content[].youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("content[].year").type(JsonFieldType.NUMBER).description("대담 영상 연도"),
                                fieldWithPath("content[].hasQuiz").type(JsonFieldType.BOOLEAN).description("퀴즈 여부(존재시 true)"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING).description("대담 영상 생성일"),
                                fieldWithPath("content[].updatedAt").type(JsonFieldType.STRING).description("대담 영상 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("대담 영상을 1개 조회할 수 있다.")
    void getTalk() throws Exception {
        //given
        Long id = 1L;
        TalkResponse response = new TalkResponse(id, "title", "youtubeId", 2023, true, LocalDateTime.now(), LocalDateTime.now());
        when(talkService.getTalkById(anyLong())).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(
                get("/talks/{talkId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("talkId").description("조회할 대담 영상의 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("대담 영상 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("대담 영상 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("대담 영상 연도"),
                                fieldWithPath("hasQuiz").type(JsonFieldType.BOOLEAN).description("퀴즈 여부(존재시 true)"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("대담 영상 생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("대담 영상 수정일")
                        )
                ));

    }

    @Test
    @DisplayName("대담 영상을 수정할 수 있다.")
    void updateTalk() throws Exception {
        //given
        Long id = 1L;
        TalkRequest request = new TalkRequest("updated title", "updated youtube id", false, 2021);
        TalkResponse response = new TalkResponse(id, "updated title", "updated youtube id", 2021, true, LocalDateTime.now(), LocalDateTime.now());
        when(talkService.updateTalk(anyLong(), any(TalkRequest.class))).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(
                put("/talks/{talkId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("talkId").description("수정할 대담 영상의 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("대담 영상 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("대담 영상 연도"),
                                fieldWithPath("hasQuiz").type(JsonFieldType.BOOLEAN).description("퀴즈 여부(존재시 true)")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("대담 영상 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("대담 영상 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("대담 영상 연도"),
                                fieldWithPath("hasQuiz").type(JsonFieldType.BOOLEAN).description("퀴즈 여부(존재시 true)"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("대담 영상 생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("대담 영상 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("대담 영상을 삭제할 수 있다.")
    void deleteTalk() throws Exception {
        //given
        doNothing().when(talkService).deleteTalk(anyLong());

        //when
        ResultActions result = mockMvc.perform(
                delete("/talks/{talkId}", 1L)
        );

        //then
        result.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("talkId").description("삭제할 대담 영상의 ID")
                        )
                ));
    }



}
