package com.scg.stop.proposal.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.global.domain.BaseTimeEntity;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ProposalReply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;
    private ProposalReply(String title, String content, Proposal proposal) {
        this.title = title;
        this.content = content;
        this.proposal = proposal;
    }
    public static ProposalReply createProposalReply(String title, String content, Proposal proposal) {
        return new ProposalReply(title, content, proposal);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public boolean isAuthorized(User requestUser) {
        return requestUser.getUserType().equals(UserType.ADMIN) || proposal.getUser().equals(requestUser);
    }
}
