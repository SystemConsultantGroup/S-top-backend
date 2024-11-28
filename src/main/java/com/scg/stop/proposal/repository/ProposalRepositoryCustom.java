package com.scg.stop.proposal.repository;

import com.scg.stop.proposal.domain.Proposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProposalRepositoryCustom {
    Page<Proposal> filterProposals(String scope, String term, Pageable pageable);
}
