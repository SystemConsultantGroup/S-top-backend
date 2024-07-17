package com.scg.stop.domain.proposal.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.global.domain.BaseResponseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ProposalResponse extends BaseResponseEntity {

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;
}
