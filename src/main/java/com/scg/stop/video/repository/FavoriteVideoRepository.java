package com.scg.stop.video.repository;

import com.scg.stop.user.domain.User;
import com.scg.stop.video.domain.FavoriteVideo;
import com.scg.stop.video.domain.JobInterview;
import com.scg.stop.video.domain.Talk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteVideoRepository extends JpaRepository<FavoriteVideo, Long> {
    FavoriteVideo findByTalkAndUser(Talk talk, User user);
    FavoriteVideo findByJobInterviewAndUser(JobInterview jobInterview, User user);

    boolean existsByTalkAndUser(Talk talk, User user);
    boolean existsByJobInterviewAndUser(JobInterview jobInterview, User user);

    @Query("SELECT f.talk FROM FavoriteVideo f WHERE f.user = :user AND f.talk IS NOT NULL")
    List<Talk> findTalksByUser(@Param("user") User user);

    @Query("SELECT f.jobInterview FROM FavoriteVideo f WHERE f.user = :user AND f.jobInterview IS NOT NULL")
    List<JobInterview> findJobInterviewsByUser(@Param("user") User user);
}
