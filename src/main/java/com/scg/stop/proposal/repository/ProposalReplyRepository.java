package com.scg.stop.proposal.repository;

import com.scg.stop.proposal.domain.Proposal;
import com.scg.stop.proposal.domain.ProposalReply;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProposalReplyRepository extends JpaRepository<ProposalReply, Long> {

//    @Query("select p from ProposalReply p where p.proposal = :proposal")
    ProposalReply findByProposal(Proposal proposal);
}
