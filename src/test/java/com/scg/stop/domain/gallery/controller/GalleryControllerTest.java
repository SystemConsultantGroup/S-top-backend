package com.scg.stop.domain.gallery.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.domain.file.dto.response.FileResponse;
import com.scg.stop.domain.gallery.dto.request.CreateGalleryRequest;
import com.scg.stop.domain.gallery.dto.response.GalleryResponse;
import com.scg.stop.domain.gallery.service.GalleryService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = GalleryController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class GalleryControllerTest extends AbstractControllerTest {

    @MockBean
    private GalleryService galleryService;

    @Autowired
    private ObjectMapper objectMapper;

    // TODO Auth 설정 추가
    @Test
    @DisplayName("갤러리 게시글을 생성할 수 있다.")
    void createGallery() throws Exception {

        // given
        List<Long> fileIds = Arrays.asList(1L, 2L, 3L);
        List<FileResponse> fileResponses = Arrays.asList(
                new FileResponse(1L, "014eb8a0-d4a6-11ee-adac-117d766aca1d", "사진1.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(2L, "11a480c0-13fa-11ef-9047-570191b390ea", "사진2.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(3L, "1883fc70-cfb4-11ee-a387-e754bd392d45", "사진3.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now())
        );
        CreateGalleryRequest request = new CreateGalleryRequest("새내기 배움터", "2024년 새내기 배움터", 2024, 4, fileIds);
        GalleryResponse response = new GalleryResponse(
                1L,
                "새내기 배움터",
                "2024년 새내기 배움터",
                2024,
                4,
                LocalDateTime.now(),
                LocalDateTime.now(),
                fileResponses
        );
        when(galleryService.createGallery(any(CreateGalleryRequest.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                post("/galleries")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("연도"),
                                fieldWithPath("month").type(JsonFieldType.NUMBER).description("월"),
                                fieldWithPath("fileIds").type(JsonFieldType.ARRAY).description("파일 ID 리스트")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("갤러리 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("연도"),
                                fieldWithPath("month").type(JsonFieldType.NUMBER).description("월"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("수정일"),
                                fieldWithPath("files").type(JsonFieldType.ARRAY).description("파일 목록"),
                                fieldWithPath("files[].id").type(JsonFieldType.NUMBER).description("파일 ID"),
                                fieldWithPath("files[].uuid").type(JsonFieldType.STRING).description("파일 UUID"),
                                fieldWithPath("files[].name").type(JsonFieldType.STRING).description("파일 이름"),
                                fieldWithPath("files[].mimeType").type(JsonFieldType.STRING).description("파일 MIME 타입"),
                                fieldWithPath("files[].createdAt").type(JsonFieldType.STRING).description("파일 생성일"),
                                fieldWithPath("files[].updatedAt").type(JsonFieldType.STRING).description("파일 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("갤러리 게시글 목록을 조회할 수 있다.")
    void getGalleries() throws Exception {

        // given
        List<FileResponse> fileResponses = Arrays.asList(
                new FileResponse(1L, "014eb8a0-d4a6-11ee-adac-117d766aca1d", "사진1.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(2L, "11a480c0-13fa-11ef-9047-570191b390ea", "사진2.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(3L, "1883fc70-cfb4-11ee-a387-e754bd392d45", "사진3.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now())
        );
        GalleryResponse galleryResponse = new GalleryResponse(
                1L,
                "새내기 배움터",
                "2024년 새내기 배움터",
                2024,
                4,
                LocalDateTime.now(),
                LocalDateTime.now(),
                fileResponses
        );
        PageImpl<GalleryResponse> galleryResponses = new PageImpl<>(Collections.singletonList(galleryResponse));
        when(galleryService.getGalleries(anyInt(), anyInt(), any(Pageable.class))).thenReturn(galleryResponses);

        // when
        ResultActions result = mockMvc.perform(
                get("/galleries")
                        .param("year", "2024")
                        .param("month", "4")
                        .contentType(APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("year").optional().description("연도"),
                                parameterWithName("month").optional().description("월"),
                                parameterWithName("page").optional().description("페이지 번호 [default: 0]"),
                                parameterWithName("size").optional().description("페이지 크기 [default: 10]")
                        ),
                        responseFields(
                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 수"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지당 데이터 수"),
                                fieldWithPath("content").type(JsonFieldType.ARRAY).description("갤러리 목록"),
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("갤러리 ID"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("갤러리 제목"),
                                fieldWithPath("content[].content").type(JsonFieldType.STRING).description("갤러리 내용"),
                                fieldWithPath("content[].year").type(JsonFieldType.NUMBER).description("갤러리 연도"),
                                fieldWithPath("content[].month").type(JsonFieldType.NUMBER).description("갤러리 월"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING).description("갤러리 생성일"),
                                fieldWithPath("content[].updatedAt").type(JsonFieldType.STRING).description("갤러리 수정일"),
                                fieldWithPath("content[].files").type(JsonFieldType.ARRAY).description("파일 목록"),
                                fieldWithPath("content[].files[].id").type(JsonFieldType.NUMBER).description("파일 ID"),
                                fieldWithPath("content[].files[].uuid").type(JsonFieldType.STRING).description("파일 UUID"),
                                fieldWithPath("content[].files[].name").type(JsonFieldType.STRING).description("파일 이름"),
                                fieldWithPath("content[].files[].mimeType").type(JsonFieldType.STRING).description("파일 MIME 타입"),
                                fieldWithPath("content[].files[].createdAt").type(JsonFieldType.STRING).description("파일 생성일"),
                                fieldWithPath("content[].files[].updatedAt").type(JsonFieldType.STRING).description("파일 수정일"),
                                fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보"),
                                fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 정보"),
                                fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 정보"),
                                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 데이터 수"),
                                fieldWithPath("pageable").ignored(),
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("빈 페이지 여부")
                        )
                ));
    }

    @Test
    @DisplayName("id로 갤러리 게시글을 조회할 수 있다.")
    void getGallery() throws Exception {

        // given
        List<FileResponse> fileResponses = Arrays.asList(
                new FileResponse(1L, "014eb8a0-d4a6-11ee-adac-117d766aca1d", "사진1.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(2L, "11a480c0-13fa-11ef-9047-570191b390ea", "사진2.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now()),
                new FileResponse(3L, "1883fc70-cfb4-11ee-a387-e754bd392d45", "사진3.jpg", "image/jpeg", LocalDateTime.now(), LocalDateTime.now())
        );
        GalleryResponse galleryResponse = new GalleryResponse(
                1L,
                "새내기 배움터",
                "2024년 새내기 배움터",
                2024,
                4,
                LocalDateTime.now(),
                LocalDateTime.now(),
                fileResponses
        );
        when(galleryService.getGallery(1L)).thenReturn(galleryResponse);

        // when
        ResultActions result = mockMvc.perform(
                get("/galleries/{galleryId}", 1L)
                        .contentType(APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("galleryId").description("조회할 갤러리 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("갤러리 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("year").type(JsonFieldType.NUMBER).description("연도"),
                                fieldWithPath("month").type(JsonFieldType.NUMBER).description("월"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("수정일"),
                                fieldWithPath("files").type(JsonFieldType.ARRAY).description("파일 목록"),
                                fieldWithPath("files[].id").type(JsonFieldType.NUMBER).description("파일 ID"),
                                fieldWithPath("files[].uuid").type(JsonFieldType.STRING).description("파일 UUID"),
                                fieldWithPath("files[].name").type(JsonFieldType.STRING).description("파일 이름"),
                                fieldWithPath("files[].mimeType").type(JsonFieldType.STRING).description("파일 MIME 타입"),
                                fieldWithPath("files[].createdAt").type(JsonFieldType.STRING).description("파일 생성일"),
                                fieldWithPath("files[].updatedAt").type(JsonFieldType.STRING).description("파일 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("갤러리 게시글을 삭제할 수 있다.")
    void deleteGallery() throws Exception {

        // given
        Long galleryId = 1L;
        doNothing().when(galleryService).deleteGallery(galleryId);

        // when
        ResultActions result = mockMvc.perform(
                delete("/galleries/{galleryId}", galleryId)
                        .contentType(APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("galleryId").description("삭제할 갤러리 ID")
                        )
                ));
    }
}