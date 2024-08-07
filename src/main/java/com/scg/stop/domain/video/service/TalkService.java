package com.scg.stop.domain.video.service;

import com.scg.stop.domain.video.domain.Quiz;
import com.scg.stop.domain.video.domain.Talk;
import com.scg.stop.domain.video.dto.request.TalkRequest;
import com.scg.stop.domain.video.dto.response.TalkResponse;
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
    public Page<TalkResponse> getTalks(String title, Integer year, Pageable pageable) {
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
        Talk talk = talkRepository.save(Talk.from(
                talkRequest.getTitle(),
                talkRequest.getYoutubeId(),
                talkRequest.getYear(),
                talkRequest.getTalkerBelonging(),
                talkRequest.getTalkerName()
        ));
        if(talkRequest.getQuiz().getQuiz() != null) {
            Quiz quiz = quizRepository.save(Quiz.from(talkRequest.getQuiz().toQuizInfoMap()));
            talk.setQuiz(quiz);
        }
        return TalkResponse.from(talk);
    }

    public TalkResponse updateTalk(Long id, TalkRequest talkRequest) {
        Talk talk = talkRepository.findById(id).orElseThrow(
                () -> new BadRequestException(ExceptionCode.TALK_ID_NOT_FOUND)
        );

        if(talk.getQuiz() != null && talkRequest.getQuiz().getQuiz() == null) { // 기존에 퀴즈가 있는데, 퀴즈를 지울때
            quizRepository.delete(talk.getQuiz());
            talk.setQuiz(null);
        }
        else if(talk.getQuiz() != null) { // 기존에 퀴즈가 있어, 퀴즈를 업데이트 할 때
            talk.getQuiz().updateQuiz(talkRequest.quiz.toQuizInfoMap());
        }
        else if(talkRequest.getQuiz().getQuiz() != null) { // 기존에 없고, 새로 퀴즈가 생길때
            Quiz quiz = quizRepository.save(Quiz.from(talkRequest.getQuiz().toQuizInfoMap()));
            talk.setQuiz(quiz);
        }
        talk.updateTalk(
                talkRequest.getTitle(),
                talkRequest.getYoutubeId(),
                talkRequest.getYear(),
                talkRequest.getTalkerBelonging(),
                talkRequest.getTalkerName()
        );
        return TalkResponse.from(talk);
    }

    public void deleteTalk(Long id) {
        Talk talk = talkRepository.findById(id).orElseThrow(
                () -> new BadRequestException(ExceptionCode.TALK_ID_NOT_FOUND)
        );
        talkRepository.delete(talk);
    }

}
