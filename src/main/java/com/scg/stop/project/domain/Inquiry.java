package com.scg.stop.project.domain;

import com.scg.stop.global.domain.BaseTimeEntity;
import com.scg.stop.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Inquiry extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = LAZY, mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true)
    private InquiryReply reply;

    private Inquiry(String title, String content, Project project, User user) {
        this.title = title;
        this.content = content;
        this.project = project;
        this.user = user;
    }

    public static Inquiry createInquiry(String title, String content, Project project, User user) {
        return new Inquiry(title, content, project, user);
    }

    public void updateInquiry(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
