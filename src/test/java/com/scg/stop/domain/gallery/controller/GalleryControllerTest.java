package com.scg.stop.domain.gallery.controller;

import static org.junit.jupiter.api.Assertions.*;
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
import com.scg.stop.domain.file.dto.response.FileResponse;
import com.scg.stop.domain.gallery.dto.request.CreateGalleryRequest;
import com.scg.stop.domain.gallery.dto.response.GalleryResponse;
import com.scg.stop.domain.gallery.service.GalleryService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
}