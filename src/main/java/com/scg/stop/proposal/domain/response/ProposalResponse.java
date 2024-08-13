package com.scg.stop.proposal.domain.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProposalResponse {

    private String title;
    private String name;
    private LocalDateTime createdDate;

    public static  ProposalResponse of(String title, String name, LocalDateTime date) {
        return new ProposalResponse(
                title,
                name,
                date
        );
    }
}
