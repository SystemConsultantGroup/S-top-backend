package com.scg.stop.user.domain;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.scg.stop.auth.domain.request.RegisterRequest;
import com.scg.stop.domain.project.domain.Comment;
import com.scg.stop.domain.project.domain.FavoriteProject;
import com.scg.stop.domain.project.domain.Inquiry;
import com.scg.stop.domain.project.domain.Likes;
import com.scg.stop.domain.proposal.domain.Proposal;
import com.scg.stop.domain.video.domain.FavoriteVideo;
import com.scg.stop.domain.video.domain.UserQuiz;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
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

    public User(String socialLoginId) {
        this.socialLoginId = socialLoginId;
    }

    public void register(String name, String email, String phone, UserType userType, Student studentInfo, String signupSource ) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        if (studentInfo != null)  this.studentInfo = studentInfo;
        this.signupSource = signupSource;
    }
}
