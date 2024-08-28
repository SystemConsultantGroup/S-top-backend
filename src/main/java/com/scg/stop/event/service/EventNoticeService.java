package com.scg.stop.event.service;

import com.scg.stop.file.domain.File;
import com.scg.stop.file.repository.FileRepository;
import com.scg.stop.event.domain.EventNotice;
import com.scg.stop.event.dto.request.EventNoticeRequest;
import com.scg.stop.event.dto.response.EventNoticeListElementResponse;
import com.scg.stop.event.dto.response.EventNoticeResponse;
import com.scg.stop.event.repository.EventNoticeRepository;
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
public class EventNoticeService {

    private final EventNoticeRepository eventNoticeRepository;
    private final FileRepository fileRepository;

    /**
     * Create a new eventNotice
     *
     * @param request EventNotice Request DTO
     * @return EventNotice Response DTO
     */
    public EventNoticeResponse createEventNotice(EventNoticeRequest request) {
        List<File> attachedFiles = getAttachedFiles(request.getFileIds());
        EventNotice newEventNotice = EventNotice.from(
                request.getTitle(),
                request.getContent(),
                request.isFixed(),
                attachedFiles
        );
        eventNoticeRepository.save(newEventNotice);
        return EventNoticeResponse.from(newEventNotice, attachedFiles);
    }

    /**
     * Get a list of eventNotices
     *
     * @param title    Title of the eventNotice (optional)
     * @param pageable Pageable
     * @return List of eventNotices
     */
    @Transactional(readOnly = true)
    public Page<EventNoticeListElementResponse> getEventNoticeList(String title, Pageable pageable) {
        List<EventNoticeListElementResponse> fixedEventNotices = eventNoticeRepository.findFixedEventNotices(title);
        int nonFixedEventNoticesSize = pageable.getPageSize() - fixedEventNotices.size();
        Pageable adjustedPageable = PageRequest.of(pageable.getPageNumber(), nonFixedEventNoticesSize);

        Page<EventNoticeListElementResponse> nonFixedEventNotices = eventNoticeRepository.findNonFixedEventNotices(title, adjustedPageable);

        List<EventNoticeListElementResponse> combinedEventNotices = new ArrayList<>();
        combinedEventNotices.addAll(fixedEventNotices);
        combinedEventNotices.addAll(nonFixedEventNotices.getContent());

        long totalElements = fixedEventNotices.size() + nonFixedEventNotices.getTotalElements();
        int totalPages = (int) Math.ceil((double) nonFixedEventNotices.getTotalElements() / adjustedPageable.getPageSize());

        return new PageImpl<>(combinedEventNotices, pageable, totalElements) {
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
     * Get a corresponding eventNotice
     *
     * @param eventNoticeId ID of the eventNotice
     * @return EventNotice Response DTO
     */
    public EventNoticeResponse getEventNotice(Long eventNoticeId) {
        EventNotice eventNotice = eventNoticeRepository.findById(eventNoticeId).orElseThrow(() ->
                new BadRequestException(ExceptionCode.EVENT_NOTICE_NOT_FOUND));
        eventNotice.increaseHitCount();
        return EventNoticeResponse.from(eventNotice, eventNotice.getFiles());
    }

    /**
     * Update a corresponding eventNotice
     *
     * @param eventNoticeId ID of the eventNotice
     * @param request       EventNotice Request DTO
     * @return EventNotice Response DTO
     */
    public EventNoticeResponse updateEventNotice(Long eventNoticeId, EventNoticeRequest request) {
        EventNotice eventNotice = eventNoticeRepository.findById(eventNoticeId).orElseThrow(() ->
                new BadRequestException(ExceptionCode.EVENT_NOTICE_NOT_FOUND));
        List<File> attachedFiles = getAttachedFiles(request.getFileIds());

        eventNotice.updateEventNotice(request.getTitle(), request.getContent(), request.isFixed(), attachedFiles);
        eventNoticeRepository.save(eventNotice);
        return EventNoticeResponse.from(eventNotice, attachedFiles);
    }

    /**
     * Delete a corresponding eventNotice
     *
     * @param eventNoticeId ID of the eventNotice
     */
    public void deleteEventNotice(Long eventNoticeId) {
        EventNotice eventNotice = eventNoticeRepository.findById(eventNoticeId).orElseThrow(() ->
                new BadRequestException(ExceptionCode.EVENT_NOTICE_NOT_FOUND));
        eventNoticeRepository.delete(eventNotice);
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