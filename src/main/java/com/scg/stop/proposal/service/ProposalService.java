package com.scg.stop.proposal.service;

import static com.scg.stop.proposal.domain.Proposal.convertProjectTypesToString;
import static com.scg.stop.proposal.domain.Proposal.convertStringToProjectTypes;

import com.scg.stop.file.domain.File;
import com.scg.stop.file.dto.response.FileResponse;
import com.scg.stop.file.repository.FileRepository;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.global.infrastructure.EmailService;
import com.scg.stop.proposal.domain.Proposal;
import com.scg.stop.proposal.domain.ProposalReply;
import com.scg.stop.proposal.domain.request.CreateProposalRequest;
import com.scg.stop.proposal.domain.request.ProposalReplyRequest;
import com.scg.stop.proposal.domain.response.ProposalDetailResponse;
import com.scg.stop.proposal.domain.response.ProposalReplyResponse;
import com.scg.stop.proposal.domain.response.ProposalResponse;
import com.scg.stop.proposal.repository.ProposalReplyRepository;
import com.scg.stop.proposal.repository.ProposalRepository;
import com.scg.stop.user.domain.User;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final ProposalReplyRepository proposalReplyRepository;
    private final EmailService emailService;
    private final FileRepository fileRepository;
    @Transactional(readOnly = true)
    public Page<ProposalResponse> getProposalList(String scope, String term, Pageable pageable, User requestUser) {
        Page<Proposal> proposals = proposalRepository.filterProposals(scope, term, pageable);
        return proposals
                .map(proposal ->
                        ProposalResponse.of(
                                proposal.getId(),
                                proposal.getDisplayTitle(requestUser),
                                proposal.getDisplayUser(requestUser),
                                proposal.getCreatedAt()
                        )
                );
    }

    @Transactional(readOnly = true)
    public ProposalDetailResponse getProposalDetail(Long proposalId, User requestUser) {
        Proposal proposal = proposalRepository.findByIdWithFiles(proposalId)
                .orElseThrow(()-> new BadRequestException(ExceptionCode.NOT_FOUND_PROPOSAL));
        if (!proposal.isAuthorized(requestUser)) throw new BadRequestException(ExceptionCode.NOT_AUTHORIZED);
        return ProposalDetailResponse.of(
                proposal.getId(),
                proposal.getUser().getName(),
                proposal.getEmail(),
                proposal.getWebSite(),
                proposal.getTitle(),
                convertStringToProjectTypes(proposal.getProjectTypes()),
                proposal.getContent(),
                proposal.getProposalReply() != null,
                proposal.getFiles().stream()
                        .map(FileResponse::from)
                        .toList()
        );
    }

    @Transactional
    public ProposalDetailResponse createProposal(User user, CreateProposalRequest proposalCreateRequest) {
        List<File> files = fileRepository.findByIdIn(proposalCreateRequest.getFileIds());
        Proposal proposal = Proposal.createProposal(
                user,
                proposalCreateRequest.getTitle(),
                convertProjectTypesToString(proposalCreateRequest.getProjectTypes()),
                proposalCreateRequest.getEmail(),
                proposalCreateRequest.getWebSite(),
                proposalCreateRequest.getContent(),
                proposalCreateRequest.getIsVisible(),
                proposalCreateRequest.getIsAnonymous(),
                files
        );
        proposalRepository.save(proposal);
        //TODO: 이메일 형식 정하기  & 과제 제안메일은 어드민 이메일로만 보내면 되는지?
        emailService.sendEmail(proposal.getEmail(), proposal.getTitle(), proposal.getContent());
        return ProposalDetailResponse.of(
                proposal.getId(),
                proposal.getUser().getName(),
                proposal.getEmail(),
                proposal.getWebSite(),
                proposal.getTitle(),
                convertStringToProjectTypes(proposal.getProjectTypes()),
                proposal.getContent(),
                proposal.getProposalReply() != null,
                proposal.getFiles().stream()
                        .map(FileResponse::from)
                        .toList()
                );
    }

    @Transactional
    public ProposalDetailResponse updateProposal(Long proposalId, CreateProposalRequest proposalUpdateRequest, User requestUser) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROPOSAL));
        if (!proposal.isAuthorized(requestUser)) throw new BadRequestException(ExceptionCode.NOT_AUTHORIZED);

        List<File> files = fileRepository.findByIdIn(proposalUpdateRequest.getFileIds());

        proposal.update(
                proposalUpdateRequest.getTitle(),
                convertProjectTypesToString(proposalUpdateRequest.getProjectTypes()),
                proposalUpdateRequest.getEmail(),
                proposalUpdateRequest.getWebSite(),
                proposalUpdateRequest.getContent(),
                proposalUpdateRequest.getIsVisible(),
                proposalUpdateRequest.getIsAnonymous(),
                files
        );
//        emailService.sendEmail();
        return ProposalDetailResponse.of(
                proposal.getId(),
                proposal.getUser().getName(),
                proposal.getEmail(),
                proposal.getWebSite(),
                proposal.getTitle(),
                convertStringToProjectTypes(proposal.getProjectTypes()),
                proposal.getContent(),
                proposal.getProposalReply() != null,
                proposal.getFiles().stream().
                        map(FileResponse::from)
                        .toList()
        );
    }

    @Transactional
    public ProposalReplyResponse createProposalReply(Long proposalId,
                                                     ProposalReplyRequest proposalReplyCreateRequest) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROPOSAL));
        ProposalReply proposalReply = ProposalReply.createProposalReply(proposalReplyCreateRequest.getTitle(),
                proposalReplyCreateRequest.getContent(),
                proposal);
        proposalReplyRepository.save(proposalReply);
        return ProposalReplyResponse.of(proposalReply.getId(), proposalReply.getTitle(), proposalReply.getContent());
    }

    @Transactional
    public ProposalReplyResponse updateProposalReply(Long proposalReplyId,
                                                     ProposalReplyRequest proposalReplyUpdateRequest) {
        ProposalReply proposalReply = proposalReplyRepository.findById(proposalReplyId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROPOSALREPLY));

        proposalReply.update(proposalReplyUpdateRequest.getTitle(), proposalReplyUpdateRequest.getContent());
        return ProposalReplyResponse.of(proposalReply.getId(), proposalReply.getTitle(), proposalReply.getContent());
    }

    @Transactional
    public void deleteProposalReply(Long proposalReplyId) {
        ProposalReply proposalReply = proposalReplyRepository.findById(proposalReplyId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROPOSALREPLY));

        proposalReplyRepository.delete(proposalReply);
    }

    @Transactional
    public void deleteProposal(Long proposalId, User requestUser) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROPOSAL));
        if (!proposal.isAuthorized(requestUser)) throw new BadRequestException(ExceptionCode.NOT_AUTHORIZED);
        proposalRepository.delete(proposal);
    }

    //TODO: 답변이 복수일경우
//    @Transactional(readOnly = true)
//    public List<ProposalReplyResponse> getProposalReplies(Long proposalId) {
//        Proposal proposal = proposalRepository.findById(proposalId)
//                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROPOSAL));
//        return proposalReplyRepository.findByProposal(proposal)
//                .stream()
//                .map((proposalReply) -> ProposalReplyResponse.of(proposalReply.getId(), proposalReply.getTitle(), proposalReply.getContent()))
//                .collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public ProposalReplyResponse getProposalReply(Long proposalId, User requestUser) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROPOSAL));
        ProposalReply proposalReply = proposalReplyRepository.findByProposal(proposal);
        if (!proposalReply.isAuthorized(requestUser)) throw new BadRequestException(ExceptionCode.NOT_AUTHORIZED);
        return ProposalReplyResponse.of(proposalReply.getId(), proposalReply.getTitle(), proposalReply.getContent());
    }
}
