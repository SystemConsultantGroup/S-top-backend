package com.scg.stop.video.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.video.controller.JobInterviewController;
import com.scg.stop.video.domain.JobInterviewCategory;
import com.scg.stop.video.dto.request.JobInterviewRequest;
import com.scg.stop.video.dto.response.JobInterviewResponse;
import com.scg.stop.video.dto.response.JobInterviewUserResponse;
import com.scg.stop.video.service.FavoriteVideoService;
import com.scg.stop.video.service.JobInterviewService;
import jakarta.servlet.http.Cookie;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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

    private static final String ACCESS_TOKEN = "admin_access_token";
    private static final String USER_ACCESS_TOKEN = "access_token";
    private static final String OPTIONAL_ACCESS_TOKEN = "optional_access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    @MockBean
    private JobInterviewService jobInterviewService;

    @MockBean
    private FavoriteVideoService favoriteVideoService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @DisplayName("잡페어 인터뷰를 생성한다.")
    void createJobInterview() throws Exception {

        // given
        JobInterviewRequest request = new JobInterviewRequest("잡페어 인터뷰의 제목", "유튜브 고유 ID", 2024, "대담자의 소속", "대담자의 성명", JobInterviewCategory.INTERN);
        JobInterviewResponse response = new JobInterviewResponse(1L, "잡페어 인터뷰의 제목", "유튜브 고유 ID", 2024,"대담자의 소속", "대담자의 성명",  JobInterviewCategory.INTERN, LocalDateTime.now(), LocalDateTime.now());

        when(jobInterviewService.createJobInterview(any())).thenReturn(response);
        // when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/jobInterviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                        .content(objectMapper.writeValueAsString(request))

        );

        // then

        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("잡페어 인터뷰 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 연도"),
                                fieldWithPath("talkerBelonging").type(JsonFieldType.STRING).description("잡페어 인터뷰 대담자의 소속"),
                                fieldWithPath("talkerName").type(JsonFieldType.STRING).description("잡페어 인터뷰 대담자의 성명"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("잡페어 인터뷰 카테고리: SENIOR, INTERN")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("잡페어 인터뷰 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 연도"),
                                fieldWithPath("talkerBelonging").type(JsonFieldType.STRING).description("잡페어 인터뷰 대담자의 소속"),
                                fieldWithPath("talkerName").type(JsonFieldType.STRING).description("잡페어 인터뷰 대담자의 성명"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("잡페어 인터뷰 카테고리: SENIOR, INTERN"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("잡페어 인터뷰 생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("잡페어 인터뷰 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("잡페어 인터뷰 리스트를 조회할 수 있다.")
    void getJobInterviewList() throws Exception {
        //given
        JobInterviewUserResponse interview1 = new JobInterviewUserResponse(1L,"잡페어 인터뷰의 제목1", "유튜브 고유 ID1", 2023,"대담자의 소속1", "대담자의 성명1", false,  JobInterviewCategory.INTERN, LocalDateTime.now(), LocalDateTime.now());
        JobInterviewUserResponse interview2 = new JobInterviewUserResponse(2L, "잡페어 인터뷰의 제목2", "유튜브 고유 ID2", 2024,"대담자의 소속2", "대담자의 성명2", true,  JobInterviewCategory.INTERN, LocalDateTime.now(), LocalDateTime.now());
        Page<JobInterviewUserResponse> page = new PageImpl<>(List.of(interview1, interview2), PageRequest.of(0,10),2);

        when(jobInterviewService.getJobInterviews(any(), any(), any(), any(), any())).thenReturn(page);

        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/jobInterviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, OPTIONAL_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰").optional()
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token").optional()
                        ),
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
                                fieldWithPath("content[].talkerBelonging").type(JsonFieldType.STRING).description("잡페어 인터뷰 대담자의 소속"),
                                fieldWithPath("content[].talkerName").type(JsonFieldType.STRING).description("잡페어 인터뷰 대담자의 성명"),
                                fieldWithPath("content[].favorite").type(JsonFieldType.BOOLEAN).description("관심에 추가한 잡페어 인터뷰 여부"),
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
        JobInterviewUserResponse response = new JobInterviewUserResponse(1L, "잡페어 인터뷰의 제목", "유튜브 고유 ID", 2024,"대담자의 소속", "대담자의 성명", false, JobInterviewCategory.INTERN, LocalDateTime.now(), LocalDateTime.now());

        when(jobInterviewService.getJobInterview(any(), any())).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/jobInterviews/{jobInterviewId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, OPTIONAL_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰").optional()
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token").optional()
                        ),
                        pathParameters(
                           parameterWithName("jobInterviewId").description("조회할 잡페어 인터뷰의 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("잡페어 인터뷰 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 연도"),
                                fieldWithPath("talkerBelonging").type(JsonFieldType.STRING).description("잡페어 인터뷰 대담자의 소속"),
                                fieldWithPath("talkerName").type(JsonFieldType.STRING).description("잡페어 인터뷰 대담자의 성명"),
                                fieldWithPath("favorite").type(JsonFieldType.BOOLEAN).description("관심에 추가한 잡페어 인터뷰 여부"),
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
        JobInterviewRequest request = new JobInterviewRequest("수정된 제목", "수정된 유튜브 ID", 2024, "수정된 대담자 소속", "수정된 대담자 성명", JobInterviewCategory.INTERN);
        JobInterviewResponse response = new JobInterviewResponse(1L, "수정된 제목", "수정된 유튜브 ID", 2024,"수정된 대담자 소속", "수정된 대담자 성명", JobInterviewCategory.INTERN, LocalDateTime.of(2021,1,1,12,0), LocalDateTime.now());

        when(jobInterviewService.updateJobInterview(any(), any(JobInterviewRequest.class))).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.put("/jobInterviews/{jobInterviewId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token")
                        ),
                        pathParameters(
                                parameterWithName("jobInterviewId").description("수정할 잡페어 인터뷰의 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("잡페어 인터뷰 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 연도"),
                                fieldWithPath("talkerBelonging").type(JsonFieldType.STRING).description("잡페어 인터뷰 대담자의 소속"),
                                fieldWithPath("talkerName").type(JsonFieldType.STRING).description("잡페어 인터뷰 대담자의 성명"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("잡페어 인터뷰 카테고리: SENIOR, INTERN")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("잡페어 인터뷰 제목"),
                                fieldWithPath("youtubeId").type(JsonFieldType.STRING).description("유튜브 영상의 고유 ID"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("잡페어 인터뷰 연도"),
                                fieldWithPath("talkerBelonging").type(JsonFieldType.STRING).description("잡페어 인터뷰 대담자의 소속"),
                                fieldWithPath("talkerName").type(JsonFieldType.STRING).description("잡페어 인터뷰 대담자의 성명"),
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
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );
        //then
        result.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token")
                        ),
                        pathParameters(
                                parameterWithName("jobInterviewId").description("삭제할 잡페어 인터뷰의 ID")
                        )
                ));
    }

    @Test
    @DisplayName("잡페어 인터뷰를 관심 등록할 수 있다.")
    void createJobInterviewFavorite() throws Exception {
        //given
        Long id = 1L;
        doNothing().when(favoriteVideoService).createJobInterviewFavorite(anyLong(), any());
        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/jobInterviews/{jobInterviewId}/favorite", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, USER_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );
        //then
        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token")
                        ),
                        pathParameters(
                                parameterWithName("jobInterviewId").description("관심 목록에 추가할 잡페어 인터뷰의 ID")
                        )
                ));
    }

    @Test
    @DisplayName("잡페어 인터뷰를 관심 등록 취소할 수 있다.")
    void deleteJobInterviewFavorite() throws Exception {
        //given
        Long id = 1L;
        doNothing().when(favoriteVideoService).deleteJobInterviewFavorite(anyLong(), any());
        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/jobInterviews/{jobInterviewId}/favorite", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, USER_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );
        //then
        result.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestCookies(
                                cookieWithName("refresh-token")
                                        .description("갱신 토큰")
                        ),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("access token")
                        ),
                        pathParameters(
                                parameterWithName("jobInterviewId").description("관심 목록에서 삭제할 잡페어 인터뷰의 ID")
                        )
                ));

    }




}
