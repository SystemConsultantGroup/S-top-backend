package com.scg.stop.video.controller;

import com.scg.stop.video.dto.response.UserQuizResultResponse;
import com.scg.stop.video.service.QuizService;
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


@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes")
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/result")
    public ResponseEntity<Page<UserQuizResultResponse>> getQuizResults(
            @RequestParam(value = "year", required = false) Integer year,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<UserQuizResultResponse> responses = quizService.getQuizResults(year, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
