package com.scg.stop.domain.notice.service;

import com.scg.stop.domain.file.domain.File;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final FileRepository fileRepository;

    /**
     * Create a new notice
     *
     * @param request Notice Request DTO
     * @return Notice Response DTO
     */
    // TODO: Admin check
    public NoticeResponse createNotice(NoticeRequest request) {
        List<File> attachedFiles = getAttachedFiles(request.getFileIds());
        Notice newNotice = Notice.from(
                request.getTitle(),
                request.getContent(),
                request.isFixed(),
                attachedFiles);
        noticeRepository.save(newNotice);

        return NoticeResponse.from(newNotice, attachedFiles);
    }

    /**
     * Get a list of notices
     *
     * @param title    Title of the notice (optional)
     * @param pageable Pageable
     * @return List of notices
     */
    @Transactional(readOnly = true)
    public Page<NoticeListElementResponse> getNoticeList(String title, Pageable pageable) {

        List<NoticeListElementResponse> fixedNotices = noticeRepository.findFixedNotices(title);
        int nonFixedNoticesSize = pageable.getPageSize() - fixedNotices.size();
        Pageable adjustedPageable = PageRequest.of(pageable.getPageNumber(), nonFixedNoticesSize);

        Page<NoticeListElementResponse> nonFixedNotices = noticeRepository.findNonFixedNotices(title, adjustedPageable);

        List<NoticeListElementResponse> combinedNotices = new ArrayList<>();
        combinedNotices.addAll(fixedNotices);
        combinedNotices.addAll(nonFixedNotices.getContent());

        long totalElements = fixedNotices.size() + nonFixedNotices.getTotalElements();
        int totalPages = (int) Math.ceil((double) nonFixedNotices.getTotalElements() / adjustedPageable.getPageSize());

        return new PageImpl<>(combinedNotices, pageable, totalElements) {
            @Override
            public int getTotalPages() {
                return totalPages;
            }

            @Override
            public long getTotalElements() {
                return totalElements;
            }
        };
    }


    /**
     * Get a corresponding notice
     *
     * @param noticeId ID of the notice
     * @return Notice Response DTO
     */
    public NoticeResponse getNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                new BadRequestException(ExceptionCode.NOTICE_NOT_FOUND));
        notice.increaseHitCount();

        return NoticeResponse.from(notice, notice.getFiles());
    }

    /**
     * Update a corresponding notice
     *
     * @param noticeId ID of the notice
     * @param request  Notice Request DTO
     * @return Notice Response DTO
     */
    // TODO: Admin check
    public NoticeResponse updateNotice(Long noticeId, NoticeRequest request) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                new BadRequestException(ExceptionCode.NOTICE_NOT_FOUND));
        notice.updateNotice(
                request.getTitle(),
                request.getContent(),
                request.isFixed());

        List<File> attachedFiles = getAttachedFiles(request.getFileIds());
        updateNoticeFiles(notice, attachedFiles);

        return NoticeResponse.from(notice, attachedFiles);
    }

    /**
     * Delete a corresponding notice
     *
     * @param noticeId ID of the notice
     */
    // TODO: Admin check
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                new BadRequestException(ExceptionCode.NOTICE_NOT_FOUND));
        noticeRepository.delete(notice);
    }

    // helper method for getting attached files
    private List<File> getAttachedFiles(List<Long> fileIds) {
        if (fileIds == null || fileIds.isEmpty()) {
            return List.of();
        }
        List<File> attachedFiles = fileRepository.findByIdIn(fileIds);
        if (attachedFiles.size() != fileIds.size()) {
            throw new BadRequestException(ExceptionCode.FILE_NOT_FOUND);
        }
        return attachedFiles;
    }

    // helper method for updating attached files
    private void updateNoticeFiles(Notice notice, List<File> attachedFiles) {
        List<File> currentFiles = notice.getFiles();
        List<File> filesToRemove = currentFiles.stream()
                .filter(file -> !attachedFiles.contains(file))
                .toList();
        filesToRemove.forEach(file -> {
            currentFiles.remove(file);
            fileRepository.delete(file);
        });
        attachedFiles.forEach(file -> {
            if (!currentFiles.contains(file)) {
                file.setNotice(notice);
                currentFiles.add(file);
            }
        });
    }

}