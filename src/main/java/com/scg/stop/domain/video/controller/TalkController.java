package com.scg.stop.domain.video.controller;

import com.scg.stop.domain.video.dto.request.TalkRequest;
import com.scg.stop.domain.video.dto.response.TalkResponse;
import com.scg.stop.domain.video.dto.response.TalksResponse;
import com.scg.stop.domain.video.service.TalkService;
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

    @PostMapping
    public ResponseEntity<TalkResponse> createTalk(@RequestBody @Valid TalkRequest talkRequest) {
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
            @RequestBody @Valid TalkRequest talkRequest
    ) {
        TalkResponse response = talkService.updateTalk(talkId, talkRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{talkId}")
    public ResponseEntity<Void> deleteTalk(@PathVariable("talkId") Long talkId) {
        talkService.deleteTalk(talkId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
