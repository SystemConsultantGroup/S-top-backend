package com.scg.stop.video.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import com.scg.stop.video.domain.Quiz;
import com.scg.stop.video.dto.request.QuizSubmitRequest;
import com.scg.stop.video.dto.response.QuizResponse;
import com.scg.stop.video.dto.response.QuizSubmitResponse;
import com.scg.stop.video.repository.QuizRepository;
import com.scg.stop.video.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/talks/{talkId}/quiz")
public class QuizController {

    public final QuizService quizService;

    @GetMapping
    public ResponseEntity<QuizResponse> getQuiz(@PathVariable("talkId") Long talkId) {
        QuizResponse quizResponse = quizService.getQuiz(talkId);
        return ResponseEntity.status(HttpStatus.OK).body(quizResponse);
    }

    @PostMapping
    public ResponseEntity<QuizSubmitResponse> submitQuiz(
            @PathVariable("talkId") Long talkId,
            @AuthUser(accessType = {AccessType.ALL}) User user,
            @RequestBody @Valid QuizSubmitRequest request
    ) {
        QuizSubmitResponse response = quizService.submitQuiz(talkId, request, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
