package com.scg.stop.user.controller;

import com.scg.stop.configuration.AbstractControllerTest;
import com.scg.stop.user.dto.response.DepartmentResponse;
import com.scg.stop.user.service.DepartmentService;
import com.scg.stop.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepartmentController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class DepartmentControllerTest extends AbstractControllerTest {

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("학과 리스트를 조회할 수 있다.")
    void getDepartments() throws Exception {
        // given
        List<DepartmentResponse> departmentResponses = Arrays.asList(
                new DepartmentResponse(1L, "소프트웨어학과"),
                new DepartmentResponse(2L, "학과2")
        );
        when(departmentService.getDepartments()).thenReturn(departmentResponses);

        // when
        ResultActions result = mockMvc.perform(
                get("/departments")
                        .contentType(APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("[].id").description("학과 ID"),
                                fieldWithPath("[].name").description("학과 이름")
                        )
                ));
    }
}