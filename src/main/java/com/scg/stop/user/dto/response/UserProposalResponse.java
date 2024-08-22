package com.scg.stop.user.dto.response;

import com.scg.stop.domain.project.domain.Inquiry;
import com.scg.stop.domain.proposal.domain.Proposal;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class UserProposalResponse {

    private Long id;
    private String title;
    private LocalDateTime createdDate;

    public static UserProposalResponse from(Proposal proposal) {
        return new UserProposalResponse(
                proposal.getId(),
                proposal.getTitle(),
                proposal.getCreatedAt()
        );
    }
}
