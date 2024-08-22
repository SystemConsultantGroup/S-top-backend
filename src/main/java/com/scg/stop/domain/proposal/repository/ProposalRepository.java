package com.scg.stop.domain.proposal.repository;

import com.scg.stop.domain.project.domain.Inquiry;
import com.scg.stop.domain.proposal.domain.Proposal;
import com.scg.stop.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByUser(User user);
}
