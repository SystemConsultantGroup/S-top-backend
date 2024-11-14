package com.scg.stop.proposal.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.project.domain.ProjectType;
import com.scg.stop.user.domain.User;
import com.scg.stop.global.domain.BaseTimeEntity;
import com.scg.stop.user.domain.UserType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Proposal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String projectTypes;

    @Column(nullable = false)
    private String email;

    @Column()
    private String webSite;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isVisible;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isAnonymous;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = LAZY, mappedBy = "proposal", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProposalReply proposalReply;


    public static final String HIDDEN_TITLE = "비밀글";
    public static final String HIDDEN_CONTENT = "비밀글입니다";
    public static final String HIDDEN_EMAIL = "비공개 이메일";
    public static final String HIDDEN_WEBSITE = "비공개 웹사이트";
    public static final String HIDDEN_USER = "익명 사용자";


    private Proposal(User user, String title, String projectTypes, String email, String website, String content,
                     Boolean isVisible, Boolean isAnonymous) {
        this.title = title;
        this.projectTypes= projectTypes;
        this.email = email;
        this.webSite = website;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isVisible = isVisible;
        this.user = user;
    }

    public static Proposal createProposal(User user, String title, String projectTypes, String email, String website,
                                           String content, Boolean isVisible, Boolean isAnonymous) {
        return new Proposal(user, title, projectTypes, email, website, content, isVisible, isAnonymous);
    }

    public void update(String title, String projectTypes, String email, String website, String content,
                       Boolean isVisible, Boolean isAnonymous) {
        this.title = title;
        this.projectTypes = projectTypes;
        this.email = email;
        this.webSite = website;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isVisible = isVisible;
    }

    public static String convertProjectTypesToString(List<ProjectType> proposalTypes) {
        if (proposalTypes == null || proposalTypes.isEmpty()) {
            return "";
        }
        return proposalTypes.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    public static List<ProjectType> convertStringToProjectTypes(String proposalTypes) {
        if (proposalTypes == null || proposalTypes.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(proposalTypes.split(","))
                .map(ProjectType::valueOf)
                .collect(Collectors.toList());
    }

    public String getDisplayTitle(User requestUser) {
        return shouldHideContent(requestUser) ? HIDDEN_TITLE : this.title;
    }

    public String getDisplayContent(User requestUser) {
        return shouldHideContent(requestUser) ? HIDDEN_CONTENT : this.content;
    }

    public String getDisplayEmail(User requestUser) {
        return isAnonymous && !isAuthorized(requestUser) ? HIDDEN_EMAIL : this.email;
    }

    public String getDisplayWebsite(User requestUser) {
        return isAnonymous && !isAuthorized(requestUser) ? HIDDEN_WEBSITE : this.webSite;
    }

    public String getDisplayUser(User requestUser) {
        return (isAnonymous || !isVisible) && !isAuthorized(requestUser) ? HIDDEN_USER : user.getName();
    }

    private boolean shouldHideContent(User requestUser) {
        return !isVisible && !isAuthorized(requestUser);
    }

    public boolean isAuthorized(User requestUser) {
        return requestUser.getUserType() == UserType.ADMIN || this.user.equals(requestUser);
    }
}
