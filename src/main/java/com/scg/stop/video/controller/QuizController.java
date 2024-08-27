package com.scg.stop.video.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.global.excel.Excel;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import com.scg.stop.video.dto.response.UserQuizResultResponse;
import com.scg.stop.video.service.QuizService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes")
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/result")
    public ResponseEntity<Page<UserQuizResultResponse>> getQuizResults(
            @RequestParam(value = "year", required = false) Integer year,
            @AuthUser(accessType = {AccessType.ADMIN}) User user,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<UserQuizResultResponse> responses = quizService.getQuizResults(year, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/result/excel")
    public void getQuizResultToExcel(
            @RequestParam(value = "year", required = false) Integer year,
            //@AuthUser(accessType = {AccessType.ADMIN}) User user,
            HttpServletResponse response
    ) {
        Excel excel = quizService.getQuizResultToExcel(year);
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s",excel.getFilename()));
        try {
            excel.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
