package com.scg.stop.proposal.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.proposal.domain.request.CreateProposalRequest;
import com.scg.stop.proposal.domain.request.ProposalReplyRequest;
import com.scg.stop.proposal.domain.response.ProposalDetailResponse;
import com.scg.stop.proposal.domain.response.ProposalReplyResponse;
import com.scg.stop.proposal.domain.response.ProposalResponse;
import com.scg.stop.proposal.service.ProposalService;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/proposals")
public class ProposalController {

    private final ProposalService proposalService;

    @GetMapping()
    public ResponseEntity<Page<ProposalResponse>> getProposals(@AuthUser(accessType = {AccessType.COMPANY, AccessType.ADMIN}) User user,
                                                               @RequestParam(value = "title", required = false) String title,
                                                               @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<ProposalResponse> proposalResponse = proposalService.getProposalList(title, pageable, user);
        return ResponseEntity.status(HttpStatus.OK).body(proposalResponse);
    }

    @GetMapping("/{proposalId}")
    public ResponseEntity<ProposalDetailResponse> getProposal(@AuthUser(accessType = {AccessType.COMPANY, AccessType.ADMIN}) User user,
                                                              @PathVariable("proposalId") Long proposalId) {
        ProposalDetailResponse proposalDetailResponse = proposalService.getProposalDetail(proposalId, user);
        return ResponseEntity.status(HttpStatus.OK).body(proposalDetailResponse);
    }

    @PostMapping()
    public ResponseEntity<ProposalDetailResponse> createProposal(@AuthUser(accessType = {AccessType.COMPANY}) User user,
                                                                 @RequestBody @Valid CreateProposalRequest proposalCreateRequest) {
        ProposalDetailResponse proposalDetailResponse = proposalService.createProposal(user, proposalCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(proposalDetailResponse);
    }

    @PutMapping("/{proposalId}")
    public ResponseEntity<ProposalDetailResponse> updateProposal(@AuthUser(accessType = {AccessType.COMPANY, AccessType.ADMIN}) User user,
                                                                 @PathVariable("proposalId") Long proposalId,
                                                                 @RequestBody @Valid CreateProposalRequest proposalUpdateRequest) {
        ProposalDetailResponse proposalDetailResponse = proposalService.updateProposal(proposalId,
                proposalUpdateRequest, user);
        return ResponseEntity.status(HttpStatus.OK).body(proposalDetailResponse);
    }

    @DeleteMapping("/{proposalId}")
    public ResponseEntity<Void> deleteProposal(@AuthUser(accessType = {AccessType.ADMIN, AccessType.COMPANY}) User user,
                                           @PathVariable("proposalId") Long proposalId) {
        proposalService.deleteProposal(proposalId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{proposalId}/reply")
    public ResponseEntity<ProposalReplyResponse> createProposalReply(@AuthUser(accessType = {AccessType.ADMIN}) User user,
                                                                     @PathVariable("proposalId") Long proposalId,
                                                                     @RequestBody @Valid ProposalReplyRequest proposalReplyCreateRequest) {
        if (user.getUserType() != UserType.ADMIN) throw new BadRequestException(ExceptionCode.NOT_AUTHORIZED);
        ProposalReplyResponse proposalReplyResponse = proposalService.createProposalReply(proposalId, proposalReplyCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(proposalReplyResponse);
    }

    @PutMapping("/{proposalId}/reply/{proposalReplyId}")
    public ResponseEntity<ProposalReplyResponse> updateProposalReply(@AuthUser(accessType = {AccessType.ADMIN}) User user,
                                                                     @PathVariable("proposalId") Long proposalId,
                                                                     @PathVariable("proposalReplyId") Long proposalReplyId,
                                                                     @RequestBody @Valid ProposalReplyRequest proposalReplyUpdateRequest) {

        if (user.getUserType() != UserType.ADMIN) throw new BadRequestException(ExceptionCode.NOT_AUTHORIZED);
        ProposalReplyResponse proposalReplyResponse = proposalService.updateProposalReply(proposalReplyId, proposalReplyUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(proposalReplyResponse);
    }
    @DeleteMapping("/{proposalId}/reply/{proposalReplyId}")
    public ResponseEntity<Void> deleteProposalReply(@AuthUser(accessType = {AccessType.ADMIN}) User user,
                                           @PathVariable("proposalId") Long proposalId, @PathVariable("proposalReplyId") Long proposalReplyId) {
        if (user.getUserType() != UserType.ADMIN) throw new BadRequestException(ExceptionCode.NOT_AUTHORIZED);
        proposalService.deleteProposalReply(proposalReplyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{proposalId}/reply")
    public ResponseEntity<ProposalReplyResponse> getProposalReplies(@AuthUser(accessType = {AccessType.ADMIN}) User user,
                                                                    @PathVariable("proposalId") Long proposalId) {
//        List<ProposalReplyResponse> proposalReplies = proposalService.getProposalReplies(proposalId);
        ProposalReplyResponse proposalReply = proposalService.getProposalReply(proposalId, user);
        return ResponseEntity.status(HttpStatus.OK).body(proposalReply);
    }
}
