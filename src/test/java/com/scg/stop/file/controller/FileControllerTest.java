package com.scg.stop.file.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.file.domain.File;
import com.scg.stop.file.dto.response.FileResponse;
import com.scg.stop.file.service.FileService;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
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
    @DisplayName("여러개의 파일을 업로드할 수 있다.")
    void uploadFiles() throws Exception {
        // given
        MockMultipartFile mockFile1 = new MockMultipartFile(
                "files",
                "첨부파일1.png",
                "image/png",
                "[BINARY DATA - PNG IMAGE CONTENT]".getBytes(StandardCharsets.UTF_8)
        );
        MockMultipartFile mockFile2 = new MockMultipartFile(
                "files",
                "첨부파일2.pdf",
                "application/pdf",
                "[BINARY DATA - PDF CONTENT]".getBytes(StandardCharsets.UTF_8)
        );
        FileResponse response1 = new FileResponse(
                1L,
                UUID.randomUUID().toString(),
                "첨부파일1.png",
                "image/png",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        FileResponse response2 = new FileResponse(
                2L,
                UUID.randomUUID().toString(),
                "첨부파일2.pdf",
                "application/pdf",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        List<FileResponse> responses = List.of(response1, response2);

        when(fileService.uploadFiles(anyList())).thenReturn(responses);

        // when
        ResultActions result = mockMvc.perform(
                multipart("/files")
                        .file(mockFile1)
                        .file(mockFile2)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        // then
        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestParts(
                                partWithName("files").description("업로드할 파일 리스트")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("파일 ID"),
                                fieldWithPath("[].uuid").type(JsonFieldType.STRING).description("파일 UUID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("파일 이름"),
                                fieldWithPath("[].mimeType").type(JsonFieldType.STRING).description("파일의 MIME 타입"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("파일 생성일"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("파일 수정일")
                        )
                ));
    }

    @Test
    @DisplayName("파일을 가져올 수 있다.")
    void getFile() throws Exception {
        // given
        Long fileId = 1L;
        String fileName = "s-top 로고.png";
        String mimeType = "image/png";
        byte[] fileContent = "[BINARY DATA - PNG IMAGE CONTENT]".getBytes(StandardCharsets.UTF_8);

        InputStream stream = new ByteArrayInputStream(fileContent);
        File file = File.of(UUID.randomUUID().toString(), fileName, mimeType);

        when(fileService.getFile(fileId)).thenReturn(stream);
        when(fileService.getFileMetadata(fileId)).thenReturn(file);

        // when
        ResultActions result = mockMvc.perform(
                get("/files/{fileId}", fileId)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.parseMediaType(file.getMimeType())))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("fileId").description("파일 ID")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("파일의 MIME 타입")
                        )
                ));
    }
}