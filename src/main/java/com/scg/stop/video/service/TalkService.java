package com.scg.stop.video.service;

import com.scg.stop.user.domain.User;
import com.scg.stop.video.domain.FavoriteVideo;
import com.scg.stop.video.domain.Quiz;
import com.scg.stop.video.domain.Talk;
import com.scg.stop.video.dto.request.TalkRequest;
import com.scg.stop.video.dto.response.TalkResponse;
import com.scg.stop.video.dto.response.TalkUserResponse;
import com.scg.stop.video.repository.FavoriteVideoRepository;
import com.scg.stop.video.repository.QuizRepository;
import com.scg.stop.video.repository.TalkRepository;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TalkService {
    private final TalkRepository talkRepository;
    private final QuizRepository quizRepository;
    private final FavoriteVideoRepository favoriteVideoRepository;

    @Transactional(readOnly = true)
    public Page<TalkUserResponse> getTalks(User user, String title, Integer year, Pageable pageable) {
        Page<Talk> talks = talkRepository.findPages(title, year, pageable);
        if(user == null) return talks.map(TalkUserResponse::from);
        List<TalkUserResponse> content = talks.stream().map(talk ->
                TalkUserResponse.from(talk, favoriteVideoRepository.existsByTalkAndUser(talk, user))
        ).toList();
        return new PageImpl<>(content, pageable, talks.getTotalElements());
    }

    @Transactional(readOnly = true)
    public TalkUserResponse getTalkById(Long id, User user) {
        Talk talk = talkRepository.findById(id).orElseThrow(
                () -> new BadRequestException(ExceptionCode.TALK_ID_NOT_FOUND)
        );
        if(user == null) return TalkUserResponse.from(talk);
        return TalkUserResponse.from(talk, favoriteVideoRepository.existsByTalkAndUser(talk, user));
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
