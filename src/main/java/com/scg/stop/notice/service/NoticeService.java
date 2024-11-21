package com.scg.stop.notice.service;

import com.scg.stop.file.domain.File;
import com.scg.stop.file.repository.FileRepository;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.notice.domain.Notice;
import com.scg.stop.notice.dto.request.NoticeRequest;
import com.scg.stop.notice.dto.response.NoticeListElementResponse;
import com.scg.stop.notice.dto.response.NoticeResponse;
import com.scg.stop.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
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
    public NoticeResponse createNotice(NoticeRequest request) {
        List<File> attachedFiles = getAttachedFiles(request.getFileIds());
        Notice newNotice = Notice.from(
                request.getTitle(),
                request.getContent(),
                request.isFixed(),
                attachedFiles
        );
        noticeRepository.save(newNotice);
        return NoticeResponse.from(newNotice, attachedFiles);
    }

    /**
     * Get a list of notices with sorting
     *
     * @param title    Title of the notice (optional)
     * @param pageable Pageable containing sorting information
     * @return List of notices
     */
    @Transactional(readOnly = true)
    public Page<NoticeListElementResponse> getNoticeList(String searchTerm, String searchScope, Pageable pageable) {

        // If no searchTerm is provided, set searchScope to null
        if (searchTerm == null || searchTerm.isEmpty()) {
            searchScope = null;
        }

        // Retrieve the sorting from the pageable
        Sort sort = pageable.getSort();

        // Find fixed notices based on the search criteria
        List<NoticeListElementResponse> fixedNotices = noticeRepository.findFixedNotices(searchTerm, searchScope, sort);

        // Find non-fixed notices based on the search criteria
        int nonFixedNoticesSize = pageable.getPageSize() - fixedNotices.size();
        Pageable adjustedPageable = PageRequest.of(pageable.getPageNumber(), Math.max(nonFixedNoticesSize, 0), sort);
        Page<NoticeListElementResponse> nonFixedNotices = noticeRepository.findNonFixedNotices(searchTerm, searchScope, adjustedPageable);

        // Combine fixed and non-fixed notices
        List<NoticeListElementResponse> combinedNotices = new ArrayList<>();
        combinedNotices.addAll(fixedNotices);
        combinedNotices.addAll(nonFixedNotices.getContent());

        // Calculate total elements and pages
        long totalElements = fixedNotices.size() + nonFixedNotices.getTotalElements();
        int totalPages = (int) Math.ceil((double) nonFixedNotices.getTotalElements() / adjustedPageable.getPageSize());

        // Return the combined result as a Page with sorting
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
    public NoticeResponse updateNotice(Long noticeId, NoticeRequest request) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                new BadRequestException(ExceptionCode.NOTICE_NOT_FOUND));
        List<File> attachedFiles = getAttachedFiles(request.getFileIds());

        notice.updateNotice(request.getTitle(), request.getContent(), request.isFixed(), attachedFiles);
        noticeRepository.save(notice);
        return NoticeResponse.from(notice, attachedFiles);
    }

    /**
     * Delete a corresponding notice
     *
     * @param noticeId ID of the notice
     */
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                new BadRequestException(ExceptionCode.NOTICE_NOT_FOUND));
        noticeRepository.delete(notice);
    }

    /**
     * Helper method to get attached files
     *
     * @param fileIds List of file IDs
     * @return List of files
     */
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

}