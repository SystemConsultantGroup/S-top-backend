package com.scg.stop.video.service;

import com.scg.stop.event.domain.EventPeriod;
import com.scg.stop.event.repository.EventPeriodRepository;
import com.scg.stop.global.excel.Excel;
import com.scg.stop.global.excel.ExcelUtil;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.repository.UserRepository;
import com.scg.stop.video.domain.Quiz;
import com.scg.stop.video.domain.QuizInfo;
import com.scg.stop.video.domain.Talk;
import com.scg.stop.video.domain.UserQuiz;
import com.scg.stop.video.dto.request.QuizSubmitRequest;
import com.scg.stop.video.dto.response.QuizResponse;
import com.scg.stop.video.dto.response.QuizSubmitResponse;
import com.scg.stop.video.dto.response.UserQuizResultResponse;
import com.scg.stop.video.repository.QuizRepository;
import com.scg.stop.video.repository.TalkRepository;
import com.scg.stop.video.repository.UserQuizRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class QuizService {
    private final TalkRepository talkRepository;
    private final QuizRepository quizRepository;
    private final UserQuizRepository userQuizRepository;
    private final EventPeriodRepository eventPeriodRepository;
    private final UserRepository userRepository;

    private final ExcelUtil excelUtil;

    @Transactional(readOnly = true)
    public QuizResponse getQuiz(Long talkId) {
        Talk talk = talkRepository.findById(talkId).orElseThrow(
                () -> new BadRequestException(ExceptionCode.TALK_ID_NOT_FOUND)
        );
        if(talk.getQuiz() == null) throw new BadRequestException(ExceptionCode.NO_QUIZ);
        return QuizResponse.from(talk.getQuiz());
    }

    public QuizSubmitResponse submitQuiz(Long talkId, QuizSubmitRequest submitRequest, User user) {
        Talk talk = talkRepository.findById(talkId).orElseThrow(
                () -> new BadRequestException(ExceptionCode.TALK_ID_NOT_FOUND)
        );
        if(talk.getQuiz() == null) throw new BadRequestException(ExceptionCode.NO_QUIZ);
        int currentYear = LocalDateTime.now().getYear();
        if(talk.getYear() != currentYear) {
            throw new BadRequestException(ExceptionCode.MISMATCH_CURRENT_YEAR);
        }
        Quiz quiz = talk.getQuiz();
        EventPeriod currentPeriod = eventPeriodRepository.findByYear(currentYear);
        if(currentPeriod == null) {
            throw new BadRequestException(ExceptionCode.NOT_EVENT_PERIOD);
        }

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
            userRepository.save(user);
        } else {
            if(userQuiz.getTryCount() >= 3) {
                throw new BadRequestException(ExceptionCode.TOO_MANY_TRY_QUIZ);
            }
            if(!userQuiz.isSuccess())
                userQuiz.updateSuccess(isSuccess);
            else throw new BadRequestException(ExceptionCode.ALREADY_QUIZ_SUCCESS);
        }
        return new QuizSubmitResponse(userQuiz.isSuccess(), userQuiz.getTryCount());

    }

    @Transactional(readOnly = true)
    public Page<UserQuizResultResponse> getQuizResults(Integer year, Pageable pageable) {
        if(year == null) {
            year = LocalDateTime.now().getYear();
        }
        return userQuizRepository.findUserQuizResults(year, pageable);
    }

    @Transactional(readOnly = true)
    public Excel getQuizResultToExcel(Integer year) {
        if(year == null) {
            year = LocalDateTime.now().getYear();
        }
        //List<UserQuizResultResponse> lists = userQuizRepository.findAllByYear(year);
        List<UserQuizResultResponse> lists = List.of(
                new UserQuizResultResponse(1L, "김국진", "010-8390-0511", "iam@2tle.io", 10L),
                new UserQuizResultResponse(2L, "김희찬", "010-8280-0511", "iam@2tle.io", 8L),
                new UserQuizResultResponse(3L, "김치국", "010-6723-0511", "iam@2tle.io", 4L)
        );
        SXSSFWorkbook workbook = excelUtil.createExcel(lists, UserQuizResultResponse.class);
        String filename = excelUtil.getFilename(workbook, UserQuizResultResponse.class);
        return excelUtil.toExcel(filename, workbook);
    }

    @Transactional(readOnly = true)
    public QuizSubmitResponse getUserQuiz(Long talkId, User user) {
        Talk talk = talkRepository.findById(talkId).orElseThrow(
                () -> new BadRequestException(ExceptionCode.TALK_ID_NOT_FOUND)
        );
        if(talk.getQuiz() == null) throw new BadRequestException(ExceptionCode.NO_QUIZ);
        UserQuiz userQuiz = userQuizRepository.findByUserAndQuiz(user, talk.getQuiz());
        if(userQuiz == null) {
            return new QuizSubmitResponse(false, 0);
        }
        return new QuizSubmitResponse(userQuiz.isSuccess(), userQuiz.getTryCount());
    }

}
