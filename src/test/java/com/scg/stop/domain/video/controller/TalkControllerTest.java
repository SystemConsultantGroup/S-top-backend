package com.scg.stop.domain.video.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.domain.video.dto.request.QuizInfoRequest;
import com.scg.stop.domain.video.dto.request.QuizRequest;
import com.scg.stop.domain.video.dto.request.TalkRequest;
import com.scg.stop.domain.video.dto.response.QuizResponse;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, QuizInfoRequest> quizData = new HashMap<>();
        quizData.put("0", new QuizInfoRequest("질문1", 0, List.of("선지1","선지2")));
        quizData.put("1", new QuizInfoRequest("질문2", 0, List.of("선지1","선지2")));
        QuizRequest quizRequest = new QuizRequest(quizData);
        QuizResponse quizResponse = new QuizResponse(
                quizRequest.toQuizInfoMap()
        );
        TalkRequest request= new TalkRequest("제목","유튜브 고유ID", 2024, "대담자 소속", "대담자 성명",quizRequest);
        TalkResponse response = new TalkResponse(1L, "제목", "유튜브 고유ID", 2024,  "대담자 소속","대담자 성명" ,quizResponse,LocalDateTime.now(), LocalDateTime.now());

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
                                fieldWithPath("talkerBelonging").type(JsonFieldType.STRING).description("대담자의 소속된 직장/단체"),
                                fieldWithPath("talkerName").type(JsonFieldType.STRING).description("대담자의 성명"),
                                fieldWithPath("quiz").type(JsonFieldType.OBJECT).description("퀴즈 데이터, 없는경우 null").optional(),
                                fieldWithPath("quiz.*").type(JsonFieldType.OBJECT).description("퀴즈 1개").optional(),
                                fieldWithPath("quiz.*.question").type(JsonFieldType.STRING).description("퀴즈 1개의 질문").optional(),
                                fieldWithPath("quiz.*.answer").type(JsonFieldType.NUMBER).description("퀴즈 1개의 정답선지 인덱스").optional(),
                                fieldWithPath("quiz.*.options").type(JsonFieldType.ARRAY).description("퀴즈 1개의 정답선지 리스트").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("대담 영상 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("대담 영상 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("대담 영상 연도"),
                                fieldWithPath("talkerBelonging").type(JsonFieldType.STRING).description("대담자의 소속된 직장/단체"),
                                fieldWithPath("talkerName").type(JsonFieldType.STRING).description("대담자의 성명"),
                                fieldWithPath("quiz").type(JsonFieldType.OBJECT).description("퀴즈 데이터, 없는경우 null"),
                                fieldWithPath("quiz.*").type(JsonFieldType.OBJECT).description("퀴즈 1개").optional(),
                                fieldWithPath("quiz.*.question").type(JsonFieldType.STRING).description("퀴즈 1개의 질문").optional(),
                                fieldWithPath("quiz.*.answer").type(JsonFieldType.NUMBER).description("퀴즈 1개의 정답선지 인덱스").optional(),
                                fieldWithPath("quiz.*.options").type(JsonFieldType.ARRAY).description("퀴즈 1개의 정답선지 리스트").optional(),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("대담 영상 생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("대담 영상 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("대담 영상 리스트를 조회할 수 있다.")
    void getTalkList() throws Exception {
        //given
        Map<String, QuizInfoRequest> quizData = new HashMap<>();
        quizData.put("0", new QuizInfoRequest("질문1", 0, List.of("선지1","선지2")));
        quizData.put("1", new QuizInfoRequest("질문2", 0, List.of("선지1","선지2")));
        QuizRequest quizRequest = new QuizRequest(quizData);
        QuizResponse quizResponse = new QuizResponse(
                quizRequest.toQuizInfoMap()
        );
        TalkResponse response = new TalkResponse(1L, "제목", "유튜브 고유ID", 2024,  "대담자 소속","대담자 성명" ,quizResponse,LocalDateTime.now(), LocalDateTime.now());
        Page<TalkResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0,10),1);

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
                                fieldWithPath("content[].talkerBelonging").type(JsonFieldType.STRING).description("대담자의 소속된 직장/단체"),
                                fieldWithPath("content[].talkerName").type(JsonFieldType.STRING).description("대담자의 성명"),
                                fieldWithPath("content[].quiz").type(JsonFieldType.OBJECT).description("퀴즈 데이터, 없는경우 null"),
                                fieldWithPath("content[].quiz.*").type(JsonFieldType.OBJECT).description("퀴즈 1개").optional(),
                                fieldWithPath("content[].quiz.*.question").type(JsonFieldType.STRING).description("퀴즈 1개의 질문").optional(),
                                fieldWithPath("content[].quiz.*.answer").type(JsonFieldType.NUMBER).description("퀴즈 1개의 정답선지 인덱스").optional(),
                                fieldWithPath("content[].quiz.*.options").type(JsonFieldType.ARRAY).description("퀴즈 1개의 정답선지 리스트").optional(),
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
        Map<String, QuizInfoRequest> quizData = new HashMap<>();
        quizData.put("0", new QuizInfoRequest("질문1", 0, List.of("선지1","선지2")));
        quizData.put("1", new QuizInfoRequest("질문2", 0, List.of("선지1","선지2")));
        QuizResponse quizResponse = new QuizResponse(
                new QuizRequest(quizData).toQuizInfoMap()
        );
        TalkResponse response = new TalkResponse(id, "제목", "유튜브 고유ID", 2024, "대담자 소속","대담자 성명" ,quizResponse,LocalDateTime.now(), LocalDateTime.now());
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
                                fieldWithPath("talkerBelonging").type(JsonFieldType.STRING).description("대담자의 소속된 직장/단체"),
                                fieldWithPath("talkerName").type(JsonFieldType.STRING).description("대담자의 성명"),
                                fieldWithPath("quiz").type(JsonFieldType.OBJECT).description("퀴즈 데이터, 없는경우 null"),
                                fieldWithPath("quiz.*").type(JsonFieldType.OBJECT).description("퀴즈 1개").optional(),
                                fieldWithPath("quiz.*.question").type(JsonFieldType.STRING).description("퀴즈 1개의 질문").optional(),
                                fieldWithPath("quiz.*.answer").type(JsonFieldType.NUMBER).description("퀴즈 1개의 정답선지 인덱스").optional(),
                                fieldWithPath("quiz.*.options").type(JsonFieldType.ARRAY).description("퀴즈 1개의 정답선지 리스트").optional(),
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
        Map<String, QuizInfoRequest> quizData = new HashMap<>();
        quizData.put("0", new QuizInfoRequest("수정한 질문1", 0, List.of("수정한 선지1","수정한 선지2")));
        quizData.put("1", new QuizInfoRequest("수정한 질문2", 0, List.of("수정한 선지1","수정한 선지2")));
        QuizRequest quizRequest = new QuizRequest(quizData);
        QuizResponse quizResponse = new QuizResponse(quizRequest.toQuizInfoMap());
        TalkRequest request= new TalkRequest("수정한 제목","수정한 유튜브 고유ID", 2024, "수정한 대담자 소속", "수정한 대담자 성명",quizRequest);
        TalkResponse response = new TalkResponse(id, "수정한 제목", "수정한 유튜브 고유ID", 2024, "수정한 대담자 소속","수정한 대담자 성명" ,quizResponse,LocalDateTime.now(), LocalDateTime.now());
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
                                fieldWithPath("talkerBelonging").type(JsonFieldType.STRING).description("대담자의 소속된 직장/단체"),
                                fieldWithPath("talkerName").type(JsonFieldType.STRING).description("대담자의 성명"),
                                fieldWithPath("quiz").type(JsonFieldType.OBJECT).description("퀴즈 데이터").optional(),
                                fieldWithPath("quiz.*").type(JsonFieldType.OBJECT).description("퀴즈 1개").optional(),
                                fieldWithPath("quiz.*.question").type(JsonFieldType.STRING).description("퀴즈 1개의 질문").optional(),
                                fieldWithPath("quiz.*.answer").type(JsonFieldType.NUMBER).description("퀴즈 1개의 정답선지 인덱스").optional(),
                                fieldWithPath("quiz.*.options").type(JsonFieldType.ARRAY).description("퀴즈 1개의 정답선지 리스트").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("대담 영상 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("대담 영상 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("대담 영상 연도"),
                                fieldWithPath("talkerBelonging").type(JsonFieldType.STRING).description("대담자의 소속된 직장/단체"),
                                fieldWithPath("talkerName").type(JsonFieldType.STRING).description("대담자의 성명"),
                                fieldWithPath("quiz").type(JsonFieldType.OBJECT).description("퀴즈 데이터, 없는경우 null"),
                                fieldWithPath("quiz.*").type(JsonFieldType.OBJECT).description("퀴즈 1개").optional(),
                                fieldWithPath("quiz.*.question").type(JsonFieldType.STRING).description("퀴즈 1개의 질문").optional(),
                                fieldWithPath("quiz.*.answer").type(JsonFieldType.NUMBER).description("퀴즈 1개의 정답선지 인덱스").optional(),
                                fieldWithPath("quiz.*.options").type(JsonFieldType.ARRAY).description("퀴즈 1개의 정답선지 리스트").optional(),
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
