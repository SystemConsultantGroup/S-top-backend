package com.scg.stop.proposal.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.user.domain.User;
import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @Column(nullable = false)
    private String website;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isVisible;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isAnonymous;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = LAZY, mappedBy = "proposal")
    private ProposalReply proposalReply;

    private Proposal(User user, String title, String projectTypes, String email, String website, String content,
                            String description, Boolean isVisible, Boolean isAnonymous) {
        this.title = title;
        this.projectTypes= projectTypes;
        this.email = email;
        this.website = website;
        this.content = content;
        this.description = description;
        this.isAnonymous = isAnonymous;
        this.isVisible = isVisible;
        this.user = user;
    }

    public static Proposal createProposal(User user, String title, String projectTypes, String email, String website,
                                           String content, String description, Boolean isVisible, Boolean isAnonymous) {
        return new Proposal(user, title, projectTypes, email, website, content, description, isVisible, isAnonymous);
    }

    public void update(String title, String projectTypes, String email, String website, String content,
                       String description, Boolean isVisible, Boolean isAnonymous) {
        this.title = title;
        this.projectTypes= projectTypes;
        this.email = email;
        this.website = website;
        this.content = content;
        this.description = description;
        this.isAnonymous = isAnonymous;
        this.isVisible = isVisible;
    }
}
