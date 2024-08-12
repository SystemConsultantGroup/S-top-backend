package com.scg.stop.video.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.scg.stop.video.service.QuizService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

@WebMvcTest(QuizController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class QuizControllerTest {
    private static final String ADMIN_ACCESS_TOKEN = "admin_access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    @MockBean
    private QuizService quizService;

    @Autowired
    private ObjectMapper objectMapper;




}
