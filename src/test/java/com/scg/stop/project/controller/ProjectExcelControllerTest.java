package com.scg.stop.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.file.dto.response.FileResponse;
import com.scg.stop.project.dto.response.ProjectExcelResponse;
import com.scg.stop.project.service.ProjectExcelService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectExcelController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class ProjectExcelControllerTest extends AbstractControllerTest {

    @MockBean
    private ProjectExcelService projectExcelService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ADMIN_ACCESS_TOKEN = "admin_access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    @Test
    @DisplayName("엑셀 양식으로 프로젝트를 일괄등록할 수 있다.")
    void createProjectExcel() throws Exception {
        // given
        MockMultipartFile excelFile = new MockMultipartFile(
                "excel",
                "project_upload_form.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "[BINARY DATA]".getBytes(StandardCharsets.UTF_8)
        );
        List<FileResponse> thumbnails = Arrays.asList(mockImageFileResponse("썸네일1.png"), mockImageFileResponse("썸네일2.png"));
        List<FileResponse> posters = Arrays.asList(mockImageFileResponse("포스터1.png"), mockImageFileResponse("포스터2.png"));
        MockMultipartFile thumbnailsFile = new MockMultipartFile(
                "thumbnails",
                "thumbnails.json",
                "application/json",
                objectMapper.writeValueAsString(thumbnails).getBytes(StandardCharsets.UTF_8)
        );
        MockMultipartFile postersFile = new MockMultipartFile(
                "posters",
                "thumbnails.json",
                "application/json",
                objectMapper.writeValueAsString(posters).getBytes(StandardCharsets.UTF_8)
        );
        ProjectExcelResponse projectExcelResponse = new ProjectExcelResponse(2);
        when(projectExcelService.createProjectExcel(any(), any(), any())).thenReturn(projectExcelResponse);

        // when
        ResultActions result = mockMvc.perform(
                multipart("/projects/excel")
                        .file(excelFile)
                        .file(thumbnailsFile)
                        .file(postersFile)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        // then
        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestParts(
                                partWithName("excel").description("업로드할 Excel 파일"),
                                partWithName("thumbnails").description("썸네일 등록 응답 JSON 파일"),
                                partWithName("posters").description("포스터 등록 응답 JSON 파일")
                        ),
                        responseFields(
                                fieldWithPath("successCount").type(JsonFieldType.NUMBER).description("생성에 성공한 프로젝트 개수")
                        )
                ));
    }

    private FileResponse mockImageFileResponse(String fileName) {
        return new FileResponse(
                1L,
                UUID.randomUUID().toString(),
                fileName,
                "image/png",
                LocalDateTime.now(),
                LocalDateTime.now());
    }
}
