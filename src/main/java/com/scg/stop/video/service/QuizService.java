package com.scg.stop.video.service;

import com.scg.stop.domain.event.domain.EventPeriod;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.user.domain.User;
import com.scg.stop.video.domain.Quiz;
import com.scg.stop.video.domain.QuizInfo;
import com.scg.stop.video.domain.UserQuiz;
import com.scg.stop.video.dto.request.QuizSubmitRequest;
import com.scg.stop.video.dto.response.QuizResponse;
import com.scg.stop.video.dto.response.QuizSubmitResponse;
import com.scg.stop.video.dto.response.UserQuizResultResponse;
import com.scg.stop.video.repository.EventPeriodRepository;
import com.scg.stop.video.repository.QuizRepository;
import com.scg.stop.video.repository.UserQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserQuizRepository userQuizRepository;
    private final EventPeriodRepository eventPeriodRepository;

    @Transactional(readOnly = true)
    public QuizResponse getQuiz(Long talkId) {
        Quiz quiz = quizRepository.findByTalkId(talkId).orElseThrow(
                () -> new BadRequestException(ExceptionCode.NO_QUIZ)
        );
        return QuizResponse.from(quiz);
    }

    public QuizSubmitResponse submitQuiz(Long talkId, QuizSubmitRequest submitRequest, User user) {
        Quiz quiz = quizRepository.findByTalkId(talkId).orElseThrow(
                () -> new BadRequestException(ExceptionCode.NO_QUIZ)
        );
        int currentYear = LocalDateTime.now().getYear();
        EventPeriod currentPeriod = eventPeriodRepository.findByYear(currentYear).orElseThrow(
                ()->new BadRequestException(ExceptionCode.NOT_EVENT_PERIOD)
        );
        LocalDateTime currentTime = LocalDateTime.now();
        if(!(currentTime.isAfter(currentPeriod.getStart()) && currentTime.isBefore(currentPeriod.getEnd()))) {
            throw new BadRequestException(ExceptionCode.NOT_EVENT_PERIOD);
        }
        boolean isSuccess = true;
        for(Map.Entry<String, QuizInfo> entry : quiz.getQuiz().entrySet()) {
            int correctAnswer = entry.getValue().getAnswer();
            int userAnswer = submitRequest.getResult().getOrDefault(entry.getKey(), -1);
            if(correctAnswer != userAnswer) {
                isSuccess = false;
                break;
            }
        }
        UserQuiz userQuiz = userQuizRepository.findByUserAndQuiz(user, quiz);
        if(userQuiz == null) {
            userQuiz = userQuizRepository.save(UserQuiz.from(user, quiz, isSuccess));
        } else {
            if(!userQuiz.isSuccess())
                userQuiz.updateSuccess(isSuccess);
            else throw new BadRequestException(ExceptionCode.ALREADY_QUIZ_SUCCESS);
        }
        return new QuizSubmitResponse(userQuiz.isSuccess(), userQuiz.getTryCount());

    }

    public Page<UserQuizResultResponse> getQuizResults(Integer year, Pageable pageable) {
        if(year == null) {
            year = LocalDateTime.now().getYear();
        }
        return userQuizRepository.findUserQuizResults(year, pageable);
    }



}
