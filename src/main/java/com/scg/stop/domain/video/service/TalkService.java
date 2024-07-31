package com.scg.stop.domain.video.service;

import com.scg.stop.domain.video.repository.QuizRepository;
import com.scg.stop.domain.video.repository.TalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TalkService {
    private final TalkRepository talkRepository;
    private final QuizRepository quizRepository;


}
