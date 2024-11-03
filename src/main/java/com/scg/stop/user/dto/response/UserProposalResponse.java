package com.scg.stop.user.dto.response;

import com.scg.stop.domain.proposal.domain.Proposal;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserProposalResponse {

    private Long id;
    private String title;
    private LocalDateTime createdDate;
    private boolean hasReply;

    public static UserProposalResponse from(Proposal proposal) {
        return new UserProposalResponse(
                proposal.getId(),
                proposal.getTitle(),
                proposal.getCreatedAt(),
                proposal.getReply() != null
        );
    }
}
