package com.scg.stop.video.repository;

import com.scg.stop.user.domain.User;
import com.scg.stop.video.domain.FavoriteVideo;
import com.scg.stop.video.domain.JobInterview;
import com.scg.stop.video.domain.Talk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteVideoRepository extends JpaRepository<FavoriteVideo, Long> {
    FavoriteVideo findByTalkAndUser(Talk talk, User user);
    FavoriteVideo findByJobInterviewAndUser(JobInterview jobInterview, User user);
}
