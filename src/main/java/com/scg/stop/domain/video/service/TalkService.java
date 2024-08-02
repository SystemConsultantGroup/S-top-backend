package com.scg.stop.domain.video.service;

import com.scg.stop.domain.video.domain.Quiz;
import com.scg.stop.domain.video.domain.Talk;
import com.scg.stop.domain.video.dto.request.TalkRequest;
import com.scg.stop.domain.video.dto.response.TalkResponse;
import com.scg.stop.domain.video.dto.response.TalksResponse;
import com.scg.stop.domain.video.repository.QuizRepository;
import com.scg.stop.domain.video.repository.TalkRepository;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TalkService {
    private final TalkRepository talkRepository;
    private final QuizRepository quizRepository;

    @Transactional(readOnly = true)
    public Page<TalksResponse> getTalks(String title, Integer year, Pageable pageable) {
        return talkRepository.findPages(title, year, pageable);
    }

    @Transactional(readOnly = true)
    public TalkResponse getTalkById(Long id) {
        Talk talk = talkRepository.findById(id).orElseThrow(
                () -> new BadRequestException(ExceptionCode.TALK_ID_NOT_FOUND)
        );
        return TalkResponse.from(talk);
    }

    public TalkResponse createTalk(TalkRequest talkRequest) {
        Talk talk = talkRepository.save(Talk.from(talkRequest));
        if(talkRequest.hasQuiz) {
            if(talkRequest.getQuiz() == null) throw new BadRequestException(ExceptionCode.NO_QUIZ);
            else {
                Quiz quiz = quizRepository.save(Quiz.from(talkRequest.getQuiz()));
                talk.setQuiz(quiz);
            }

        }
        return TalkResponse.from(talk);
    }

    public TalkResponse updateTalk(Long id, TalkRequest talkRequest) {
        Talk talk = talkRepository.findById(id).orElseThrow(
                () -> new BadRequestException(ExceptionCode.TALK_ID_NOT_FOUND)
        );
        if(talk.isHasQuiz() && !talkRequest.isHasQuiz()) { // 기존에는 퀴즈가 있었는데, 수정하면서 없어진 경우.
            talk.updateTalk(talkRequest);
            quizRepository.delete(talk.getQuiz());
            talk.setQuiz(null);
        }
        if(talkRequest.hasQuiz) {
            if(talkRequest.getQuiz() == null) throw new BadRequestException(ExceptionCode.NO_QUIZ);
            talk.updateTalk(talkRequest);
            if(talk.getQuiz() == null) {
                Quiz quiz = quizRepository.save(Quiz.from(talkRequest.getQuiz()));
                talk.setQuiz(quiz);
            } else {
                talk.getQuiz().updateQuiz(talkRequest.getQuiz());
            }
        }
        return TalkResponse.from(talk);
    }

    public void deleteTalk(Long id) {
        Talk talk = talkRepository.findById(id).orElseThrow(
                () -> new BadRequestException(ExceptionCode.TALK_ID_NOT_FOUND)
        );
        talkRepository.delete(talk);
    }

}
