package com.scg.stop.domain.video.repository;

import com.scg.stop.domain.video.domain.FavoriteVideo;
import com.scg.stop.domain.video.domain.JobInterview;
import com.scg.stop.domain.video.domain.Talk;
import com.scg.stop.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteVideoRepository extends JpaRepository<FavoriteVideo, Long> {
    @Query("SELECT f.talk FROM FavoriteVideo f WHERE f.user = :user AND f.talk IS NOT NULL")
    List<Talk> findTalksByUser(@Param("user") User user);

    @Query("SELECT f.jobInterview FROM FavoriteVideo f WHERE f.user = :user AND f.jobInterview IS NOT NULL")
    List<JobInterview> findJobInterviewsByUser(@Param("user") User user);
}
