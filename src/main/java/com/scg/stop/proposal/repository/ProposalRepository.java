package com.scg.stop.proposal.repository;

import com.scg.stop.proposal.domain.Proposal;
import com.scg.stop.proposal.domain.response.ProposalResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    //TODO: cursor 기반 pagination 과 성능 비교 + nativeQuery
    @Query("SELECT p FROM Proposal p WHERE :title IS NULL OR p.title LIKE %:title%")
    Page<Proposal> findProposals(@Param("title") String title, Pageable pageable);

}
