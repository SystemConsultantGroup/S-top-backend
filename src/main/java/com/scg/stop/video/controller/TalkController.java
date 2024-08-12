package com.scg.stop.video.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.video.dto.request.QuizSubmitRequest;
import com.scg.stop.video.dto.request.TalkRequest;
import com.scg.stop.video.dto.response.QuizResponse;
import com.scg.stop.video.dto.response.QuizSubmitResponse;
import com.scg.stop.video.dto.response.TalkResponse;
import com.scg.stop.video.service.FavoriteVideoService;
import com.scg.stop.video.service.QuizService;
import com.scg.stop.video.service.TalkService;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/talks")
public class TalkController {
    private final TalkService talkService;
    private final QuizService quizService;
    private final FavoriteVideoService favoriteVideoService;

    @PostMapping
    public ResponseEntity<TalkResponse> createTalk(
            @RequestBody @Valid TalkRequest talkRequest,
            @AuthUser(accessType = {AccessType.ADMIN}) User user
    ) {
        TalkResponse response = talkService.createTalk(talkRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<TalkResponse>> getAllTalks(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "year", required = false) Integer year,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<TalkResponse> talks = talkService.getTalks(title, year, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(talks);
    }

    @GetMapping("/{talkId}")
    public ResponseEntity<TalkResponse> getTalk(@PathVariable("talkId") Long talkId) {
        TalkResponse response = talkService.getTalkById(talkId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{talkId}")
    public ResponseEntity<TalkResponse> updateTalk(
            @PathVariable("talkId") Long talkId,
            @RequestBody @Valid TalkRequest talkRequest,
            @AuthUser(accessType = {AccessType.ADMIN}) User user
    ) {
        TalkResponse response = talkService.updateTalk(talkId, talkRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{talkId}")
    public ResponseEntity<Void> deleteTalk(
            @PathVariable("talkId") Long talkId,
            @AuthUser(accessType = {AccessType.ADMIN}) User user
    ) {
        talkService.deleteTalk(talkId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{talkId}/quiz")
    public ResponseEntity<QuizResponse> getQuiz(@PathVariable("talkId") Long talkId) {
        QuizResponse quizResponse = quizService.getQuiz(talkId);
        return ResponseEntity.status(HttpStatus.OK).body(quizResponse);
    }

    @PostMapping("/{talkId}/quiz")
    public ResponseEntity<QuizSubmitResponse> submitQuiz(
            @PathVariable("talkId") Long talkId,
            @AuthUser(accessType = {AccessType.ALL}) User user,
            @RequestBody @Valid QuizSubmitRequest request
    ) {
        QuizSubmitResponse response = quizService.submitQuiz(talkId, request, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{talkId}/favorite")
    public ResponseEntity<Void> createTalkFavorite(
            @PathVariable("talkId") Long talkId,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ) {
        favoriteVideoService.createTalkFavorite(talkId, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{talkId}/favorite")
    public ResponseEntity<Void> deleteTalkFavorite(
            @PathVariable("talkId") Long talkId,
            @AuthUser(accessType = {AccessType.ALL}) User user
    ) {
        favoriteVideoService.deleteTalkFavorite(talkId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
