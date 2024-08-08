package com.scg.stop.file.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.file.dto.response.FileResponse;
import com.scg.stop.file.service.FileService;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(FileController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class FileControllerTest extends AbstractControllerTest {

    @MockBean
    private FileService fileService;

    @Test
    @DisplayName("파일을 업로드할 수 있다.")
    void uploadFile() throws Exception {
        // given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "s-top 로고.png",
                "image/png",
                "[BINARY DATA - PNG IMAGE CONTENT]".getBytes(StandardCharsets.UTF_8)
        );
        FileResponse response = new FileResponse(
                1L,
                UUID.randomUUID().toString(),
                "s-top 로고.png",
                "image/png",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(fileService.uploadFile(any(MultipartFile.class))).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                multipart("/files")
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        // then
        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestParts(
                                partWithName("file").description("업로드할 파일")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("파일 ID"),
                                fieldWithPath("uuid").type(JsonFieldType.STRING).description("파일 UUID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("파일 이름"),
                                fieldWithPath("mimeType").type(JsonFieldType.STRING).description("파일의 MIME 타입"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("파일 생성일"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("파일 수정일")
                        )
                ));
    }
}