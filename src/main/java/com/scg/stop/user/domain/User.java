package com.scg.stop.user.domain;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.scg.stop.project.domain.Comment;
import com.scg.stop.project.domain.FavoriteProject;
import com.scg.stop.project.domain.Inquiry;
import com.scg.stop.project.domain.Likes;
import com.scg.stop.domain.proposal.domain.Proposal;
import com.scg.stop.video.domain.FavoriteVideo;
import com.scg.stop.video.domain.UserQuiz;
import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String email;

    @Column(nullable = false)
    private String socialLoginId;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private UserType userType;

    private String signupSource;

    @OneToOne(fetch = LAZY, mappedBy = "user")
    private Application application;

    @OneToOne(fetch = LAZY, mappedBy = "user")
    private Student studentInfo;

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<Proposal> proposals = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<UserQuiz> userQuizzes = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<FavoriteVideo> favoriteVideos = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<FavoriteProject> favoriteProjects = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "user")
    private List<Inquiry> inquiries = new ArrayList<>();

    public void activateUser() {
        if (userType == UserType.INACTIVE_COMPANY) {
            userType = UserType.COMPANY;
        } else if (userType == UserType.INACTIVE_PROFESSOR) {
            userType = UserType.PROFESSOR;
        }
        application.activate();
    }

    public User(String socialLoginId) {
        this.userType = UserType.TEMP;
        this.socialLoginId = socialLoginId;
    }

    public void register(String name, String email, String phone, UserType userType, String signupSource ) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.signupSource = signupSource;
    }
}
