package com.scg.stop.notice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.file.dto.response.FileResponse;
import com.scg.stop.notice.dto.request.NoticeRequest;
import com.scg.stop.notice.dto.response.NoticeListElementResponse;
import com.scg.stop.notice.dto.response.NoticeResponse;
import com.scg.stop.notice.service.NoticeService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoticeController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class NoticeControllerTest extends AbstractControllerTest {

    private static final String ACCESS_TOKEN = "admin_access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    @MockBean
    private NoticeService noticeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("공지 사항을 생성할 수 있다.")
    void createNotice() throws Exception {

        // given
        NoticeRequest request = new NoticeRequest("공지 사항 제목", "공지 사항 내용", true, List.of(1L, 2L, 3L));

        List<FileResponse> files = Arrays.asList(
                new FileResponse(1L, "014eb8a0-d4a6-11ee-adac-117d766aca1d", "예시 첨부 파일 1.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(2L, "11a480c0-13fa-11ef-9047-570191b390ea", "예시 첨부 파일 2.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(3L, "1883fc70-cfb4-11ee-a387-e754bd392d45", "예시 첨부 파일 3.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now())
        );
        NoticeResponse response = new NoticeResponse(1L, "공지 사항 제목", "공지 사항 내용", 0, true, LocalDateTime.now(), LocalDateTime.now(), files);

        when(noticeService.createNotice(any(NoticeRequest.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                post("/notices")
                        .contentType(APPLICATION_JSON)
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
                                fieldWithPath("title").type(JsonFieldType.STRING).description("공지 사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("공지 사항 내용"),
                                fieldWithPath("fixed").type(JsonFieldType.BOOLEAN).description("공지 사항 고정 여부"),
                                fieldWithPath("fileIds").type(JsonFieldType.ARRAY).description("첨부 파일 ID 목록").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("공지 사항 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("공지 사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("공지 사항 내용"),
                                fieldWithPath("hitCount").type(JsonFieldType.NUMBER).description("공지 사항 조회수"),
                                fieldWithPath("fixed").type(JsonFieldType.BOOLEAN).description("공지 사항 고정 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("공지 사항 생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("공지 사항 수정일"),
                                fieldWithPath("files").type(JsonFieldType.ARRAY).description("첨부 파일 목록"),
                                fieldWithPath("files[].id").type(JsonFieldType.NUMBER).description("파일 ID"),
                                fieldWithPath("files[].uuid").type(JsonFieldType.STRING).description("파일 고유 식별자"),
                                fieldWithPath("files[].name").type(JsonFieldType.STRING).description("파일 이름"),
                                fieldWithPath("files[].mimeType").type(JsonFieldType.STRING).description("파일 MIME 타입"),
                                fieldWithPath("files[].createdAt").type(JsonFieldType.STRING).description("파일 생성 시간"),
                                fieldWithPath("files[].updatedAt").type(JsonFieldType.STRING).description("파일 수정 시간")
                        )

                ));
    }

    @DisplayName("공지 사항 리스트를 조회할 수 있다.")
    @Test
    void getNoticeList() throws Exception {

        // given
        NoticeListElementResponse notice1 = new NoticeListElementResponse(1L, "notice 1", 10, true, LocalDateTime.now(), LocalDateTime.now());
        NoticeListElementResponse notice2 = new NoticeListElementResponse(2L, "notice 2", 10, false, LocalDateTime.now(), LocalDateTime.now());
        Page<NoticeListElementResponse> page = new PageImpl<>(List.of(notice1, notice2), PageRequest.of(0, 10), 2);

        when(noticeService.getNoticeList(any(String.class), any(String.class), any(Pageable.class))).thenReturn(page);

        // when
        ResultActions result = mockMvc.perform(
                get("/notices")
                        .param("terms", "notice")
                        .param("scope", "title")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("terms").description("검색어 (optional)").optional(),
                                parameterWithName("scope").description("검색 범위 (title, content, both) [default: both, searchTerm=null 이면 null로 초기화]").optional(),
                                parameterWithName("page").description("페이지 번호 [default: 0]").optional(),
                                parameterWithName("size").description("페이지 크기 [default: 10]").optional()
                        ),
                        responseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("공지 사항 ID"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("공지 사항 제목"),
                                fieldWithPath("content[].hitCount").type(JsonFieldType.NUMBER).description("공지 사항 조회수"),
                                fieldWithPath("content[].fixed").type(JsonFieldType.BOOLEAN).description("공지 사항 고정 여부"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING).description("공지 사항 생성일"),
                                fieldWithPath("content[].updatedAt").type(JsonFieldType.STRING).description("공지 사항 수정일"),
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


    @DisplayName("공지 사항을 조회할 수 있다.")
    @Test
    void getNotice() throws Exception {

        // given
        List<FileResponse> files = Arrays.asList(
                new FileResponse(1L, "014eb8a0-d4a6-11ee-adac-117d766aca1d", "예시 첨부 파일 1.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(2L, "11a480c0-13fa-11ef-9047-570191b390ea", "예시 첨부 파일 2.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(3L, "1883fc70-cfb4-11ee-a387-e754bd392d45", "예시 첨부 파일 3.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now())
        );
        NoticeResponse response = new NoticeResponse(1L, "공지 사항 제목", "content", 10, true, LocalDateTime.now(), LocalDateTime.now(), files);

        when(noticeService.getNotice(anyLong())).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                get("/notices/{noticeId}", 1L)
                        .contentType(APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("noticeId").description("조회할 공지 사항 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("공지 사항 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("공지 사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("공지 사항 내용"),
                                fieldWithPath("hitCount").type(JsonFieldType.NUMBER).description("공지 사항 조회수"),
                                fieldWithPath("fixed").type(JsonFieldType.BOOLEAN).description("공지 사항 고정 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("공지 사항 생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("공지 사항 수정일"),
                                fieldWithPath("files").type(JsonFieldType.ARRAY).description("첨부 파일 목록"),
                                fieldWithPath("files[].id").type(JsonFieldType.NUMBER).description("파일 ID"),
                                fieldWithPath("files[].uuid").type(JsonFieldType.STRING).description("파일 고유 식별자"),
                                fieldWithPath("files[].name").type(JsonFieldType.STRING).description("파일 이름"),
                                fieldWithPath("files[].mimeType").type(JsonFieldType.STRING).description("파일 MIME 타입"),
                                fieldWithPath("files[].createdAt").type(JsonFieldType.STRING).description("파일 생성 시간"),
                                fieldWithPath("files[].updatedAt").type(JsonFieldType.STRING).description("파일 수정 시간")
                        )

                ));
    }

    @DisplayName("공지 사항을 수정할 수 있다.")
    @Test
    void updateNotice() throws Exception {

        // given
        NoticeRequest request = new NoticeRequest("수정된 공지 사항 제목", "수정된 공지 사항 내용", false, List.of(1L, 2L, 3L));
        List<FileResponse> files = Arrays.asList(
                new FileResponse(1L, "014eb8a0-d4a6-11ee-adac-117d766aca1d", "예시 첨부 파일 1.jpg", "image/jpeg", LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.now()),
                new FileResponse(2L, "11a480c0-13fa-11ef-9047-570191b390ea", "예시 첨부 파일 2.jpg", "image/jpeg", LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.now()),
                new FileResponse(3L, "1883fc70-cfb4-11ee-a387-e754bd392d45", "예시 첨부 파일 3.jpg", "image/jpeg", LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.now())
        );
        NoticeResponse response = new NoticeResponse(1L, "수정된 공지 사항 제목", "수정된 공지 사항 내용", 10, false, LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.now(), files);

        when(noticeService.updateNotice(anyLong(), any(NoticeRequest.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                put("/notices/{noticeId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
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
                                parameterWithName("noticeId").description("수정할 공지 사항 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("공지 사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("공지 사항 내용"),
                                fieldWithPath("fixed").type(JsonFieldType.BOOLEAN).description("공지 사항 고정 여부"),
                                fieldWithPath("fileIds").type(JsonFieldType.ARRAY).description("첨부 파일 ID 목록").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("공지 사항 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("공지 사항 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("공지 사항 내용"),
                                fieldWithPath("hitCount").type(JsonFieldType.NUMBER).description("공지 사항 조회수"),
                                fieldWithPath("fixed").type(JsonFieldType.BOOLEAN).description("공지 사항 고정 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("공지 사항 생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("공지 사항 수정일"),
                                fieldWithPath("files").type(JsonFieldType.ARRAY).description("첨부 파일 목록"),
                                fieldWithPath("files[].id").type(JsonFieldType.NUMBER).description("파일 ID"),
                                fieldWithPath("files[].uuid").type(JsonFieldType.STRING).description("파일 고유 식별자"),
                                fieldWithPath("files[].name").type(JsonFieldType.STRING).description("파일 이름"),
                                fieldWithPath("files[].mimeType").type(JsonFieldType.STRING).description("파일 MIME 타입"),
                                fieldWithPath("files[].createdAt").type(JsonFieldType.STRING).description("파일 생성 시간"),
                                fieldWithPath("files[].updatedAt").type(JsonFieldType.STRING).description("파일 수정 시간")
                        )

                ));
    }


    @DisplayName("공지 사항을 삭제할 수 있다.")
    @Test
    void deleteNotice() throws Exception {

        // given
        doNothing().when(noticeService).deleteNotice(anyLong());

        // when
        ResultActions result = mockMvc.perform(
                delete("/notices/{noticeId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
        );

        // then
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
                                parameterWithName("noticeId").description("삭제할 공지 사항 ID")
                        )
                ));
    }
}
