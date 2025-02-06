package com.scg.stop.video.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.user.domain.User;
import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FavoriteVideo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "talk_id")
    private Talk talk;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "job_interview_id")
    private JobInterview jobInterview;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private FavoriteVideo(Talk talk, User user) {
        this.talk = talk;
        this.user = user;
    }

    private FavoriteVideo(JobInterview jobInterview, User user) {
        this.jobInterview = jobInterview;
        this.user = user;
    }

    public static FavoriteVideo of(Talk talk, User user) {
        return new FavoriteVideo(talk, user);
    }

    public static FavoriteVideo of(JobInterview jobInterview, User user) {
        return new FavoriteVideo(jobInterview, user);
    }


}
