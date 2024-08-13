package com.scg.stop.proposal.repository;

import com.scg.stop.proposal.domain.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
}
