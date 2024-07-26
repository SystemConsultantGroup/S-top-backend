package com.scg.stop.domain.notice.service;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.file.dto.response.FileResponse;
import com.scg.stop.domain.file.repository.FileRepository;
import com.scg.stop.domain.notice.domain.Notice;
import com.scg.stop.domain.notice.dto.request.NoticeRequest;
import com.scg.stop.domain.notice.dto.response.NoticeListElementResponse;
import com.scg.stop.domain.notice.dto.response.NoticeResponse;
import com.scg.stop.domain.notice.repository.NoticeRepository;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final FileRepository fileRepository;

    /**
     * Create a new notice
     * @param request Notice Request DTO
     * @return Notice Response DTO
     */
    // TODO: Admin check
    public NoticeResponse createNotice(NoticeRequest request) {
        List<File> attachedFiles = fileRepository.findByIdIn(request.getFileIds());
        if (attachedFiles.size() != request.getFileIds().size()) {
            throw new BadRequestException(ExceptionCode.FILE_NOT_FOUND);
        }

        Notice newNotice = Notice.from(request.getTitle(), request.getContent(), request.isFixed(), attachedFiles);
        noticeRepository.save(newNotice);

        List<FileResponse> fileResponses = attachedFiles.stream()
                .map(FileResponse::from)
                .collect(Collectors.toList());
        return NoticeResponse.from(newNotice, fileResponses);
    }

    /**
     * Get a list of notices
     * @param title Title of the notice (optional)
     * @param pageable Pageable
     * @return List of notices
     */
    @Transactional(readOnly = true)
    public Page<NoticeListElementResponse> getNoticeList(String title, Pageable pageable) {
        return noticeRepository.findNotices(title, pageable);
    }

    /**
     * Get a corresponding notice
     * @param id ID of the notice
     * @return Notice Response DTO
     */
    public NoticeResponse getNotice(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow(() ->
                new BadRequestException(ExceptionCode.NOTICE_NOT_FOUND));
        notice.increaseHitCount();

        List <FileResponse> fileResponses = notice.getFiles().stream()
                .map(FileResponse::from)
                .collect(Collectors.toList());
        return NoticeResponse.from(notice, fileResponses);
    }

    /**
     * Update a corresponding notice
     * @param id ID of the notice
     * @param request Notice Request DTO
     * @return Notice Response DTO
     */
    // TODO: Admin check
    public NoticeResponse updateNotice(Long id, NoticeRequest request) {
        Notice notice = noticeRepository.findById(id).orElseThrow(() ->
                new BadRequestException(ExceptionCode.NOTICE_NOT_FOUND));
        notice.updateNotice(request.getTitle(), request.getContent(), request.isFixed());

        List<File> attachedFiles = fileRepository.findByIdIn(request.getFileIds());
        if (attachedFiles.size() != request.getFileIds().size()) {
            throw new BadRequestException(ExceptionCode.FILE_NOT_FOUND);
        }

        // Find files that are no longer attached
        List<File> filesToRemove = new ArrayList<>(notice.getFiles());
        filesToRemove.removeAll(attachedFiles);

        // Remove files that are no longer attached from the file table
        filesToRemove.forEach(file -> {
            notice.getFiles().remove(file);
            fileRepository.delete(file);
        });

        // Add new files and set noticeId
        attachedFiles.forEach(file -> {
            if (!notice.getFiles().contains(file)) {
                file.setNotice(notice);
                notice.getFiles().add(file);
            }
        });

        List<FileResponse> fileResponses = notice.getFiles().stream()
                .map(FileResponse::from)
                .collect(Collectors.toList());

        return NoticeResponse.from(notice, fileResponses);
    }


    /**
     * Delete a corresponding notice
     * @param noticeId ID of the notice
     */
    // TODO: Admin check
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                new BadRequestException(ExceptionCode.NOTICE_NOT_FOUND));
        noticeRepository.delete(notice);
    }
}