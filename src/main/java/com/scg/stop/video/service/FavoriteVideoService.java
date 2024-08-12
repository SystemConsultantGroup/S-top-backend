package com.scg.stop.video.service;

import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.user.domain.User;
import com.scg.stop.video.domain.FavoriteVideo;
import com.scg.stop.video.domain.JobInterview;
import com.scg.stop.video.domain.Talk;
import com.scg.stop.video.repository.FavoriteVideoRepository;
import com.scg.stop.video.repository.JobInterviewRepository;
import com.scg.stop.video.repository.TalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteVideoService {
    private final FavoriteVideoRepository favoriteVideoRepository;
    private final JobInterviewRepository jobInterviewRepository;
    private final TalkRepository talkRepository;

    public void createJobInterviewFavorite(Long id, User user) {
        JobInterview jobInterview = jobInterviewRepository.findById(id).orElseThrow(() ->
                new BadRequestException(ExceptionCode.ID_NOT_FOUND));
        FavoriteVideo favoriteVideo = favoriteVideoRepository.findByJobInterviewAndUser(jobInterview, user);
        if (favoriteVideo != null) {
            throw new BadRequestException(ExceptionCode.ALREADY_FAVORITE);
        }
        FavoriteVideo newFavoriteVideo = favoriteVideoRepository.save(FavoriteVideo.of(jobInterview, user));
        jobInterview.addFavoriteVideo(newFavoriteVideo);
        user.addFavoriteVideo(newFavoriteVideo);


    }

    public void deleteJobInterviewFavorite(Long id, User user) {
        JobInterview jobInterview = jobInterviewRepository.findById(id).orElseThrow(() ->
                new BadRequestException(ExceptionCode.ID_NOT_FOUND));
        FavoriteVideo favoriteVideo = favoriteVideoRepository.findByJobInterviewAndUser(jobInterview, user);
        if(favoriteVideo == null) {
            throw new BadRequestException(ExceptionCode.NOT_FAVORITE);
        }
        jobInterview.removeFavoriteVideo(favoriteVideo);
        user.removeFavoriteVideo(favoriteVideo);
        favoriteVideoRepository.delete(favoriteVideo);
    }



    public void createTalkFavorite(Long id, User user) {
        Talk talk = talkRepository.findById(id).orElseThrow(() ->
                new BadRequestException(ExceptionCode.TALK_ID_NOT_FOUND));
        FavoriteVideo favoriteVideo = favoriteVideoRepository.findByTalkAndUser(talk, user);
        if(favoriteVideo != null) {
            throw new BadRequestException(ExceptionCode.ALREADY_FAVORITE);
        }
        FavoriteVideo newFavoriteVideo = favoriteVideoRepository.save(FavoriteVideo.of(talk, user));
        talk.addFavoriteVideo(newFavoriteVideo);
        user.addFavoriteVideo(newFavoriteVideo);

    }

    public void deleteTalkFavorite(Long id, User user) {
        Talk talk = talkRepository.findById(id).orElseThrow(() ->
                new BadRequestException(ExceptionCode.TALK_ID_NOT_FOUND));
        FavoriteVideo favoriteVideo = favoriteVideoRepository.findByTalkAndUser(talk, user);
        if(favoriteVideo == null) {
            throw new BadRequestException(ExceptionCode.NOT_FAVORITE);
        }
        talk.removeFavoriteVideo(favoriteVideo);
        user.removeFavoriteVideo(favoriteVideo);
        favoriteVideoRepository.delete(favoriteVideo);
    }
}
