package com.scg.stop.proposal.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ProposalReplyResponse {

    private Long id;
    private String title;
    private String content;

    public static ProposalReplyResponse of(Long id, String title, String content) {
        return new ProposalReplyResponse(id, title, content);
    }
}
