package com.scg.stop.project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.project.dto.response.ProjectExcelResponse;
import com.scg.stop.project.service.ProjectExcelService;
import jakarta.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
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

@WebMvcTest(ProjectExcelController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class ProjectExcelControllerTest extends AbstractControllerTest {

    private static final String ADMIN_ACCESS_TOKEN = "admin_access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    @MockBean
    private ProjectExcelService projectExcelService;

    @Test
    @DisplayName("엑셀 양식으로 프로젝트를 일괄등록할 수 있다.")
    void createProjectExcel() throws Exception {
        // given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "project_upload_form.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "[BINARY DATA]".getBytes(StandardCharsets.UTF_8)
        );
        ProjectExcelResponse projectExcelResponse = new ProjectExcelResponse(10);
        when(projectExcelService.createProjectExcel(any())).thenReturn(projectExcelResponse);

        // when
        ResultActions result = mockMvc.perform(
                multipart("/projects/excel")
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_ACCESS_TOKEN)
                        .cookie(new Cookie("refresh-token", REFRESH_TOKEN)));

        // then
        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("successCount").type(JsonFieldType.NUMBER).description("생성에 성공한 프로젝트 개수")
                        )
                ));
    }
}
