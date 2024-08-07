package com.scg.stop.domain.event.service;

import com.scg.stop.domain.event.domain.EventNotice;
import com.scg.stop.domain.event.dto.request.EventNoticeRequest;
import com.scg.stop.domain.event.dto.response.EventNoticeListElementResponse;
import com.scg.stop.domain.event.dto.response.EventNoticeResponse;
import com.scg.stop.domain.event.repository.EventNoticeRepository;
import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.file.repository.FileRepository;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EventNoticeService {

    private final EventNoticeRepository eventNoticeRepository;
    private final FileRepository fileRepository;

    /**
     * Create a new event notice
     *
     * @param request Event Notice Request DTO
     * @return Event Notice Response DTO
     */
    // TODO: Admin check
    public EventNoticeResponse createEventNotice(EventNoticeRequest request) {
        List<File> attachedFiles = getAttachedFiles(request.getFileIds());
        EventNotice newEventNotice = EventNotice.from(
                request.getTitle(),
                request.getContent(),
                request.isFixed(),
                attachedFiles);
        eventNoticeRepository.save(newEventNotice);

        return EventNoticeResponse.from(newEventNotice, attachedFiles);
    }

    /**
     * Get a list of event notices
     *
     * @param title    Title of the event notice (optional)
     * @param pageable Pageable
     * @return List of event notices Element Response DTO
     */
    @Transactional(readOnly = true)
    public Page<EventNoticeListElementResponse> getEventNoticeList(String title, Pageable pageable) {
        return eventNoticeRepository.findNotices(title, pageable);
    }

    /**
     * Get a corresponding event notice
     *
     * @param eventNoticeId ID of the event notice
     * @return Event Notice Response DTO
     */
    public EventNoticeResponse getEventNotice(Long eventNoticeId) {
        EventNotice eventNotice = eventNoticeRepository.findById(eventNoticeId).orElseThrow(() ->
                new BadRequestException(ExceptionCode.EVENT_NOTICE_NOT_FOUND));
        eventNotice.increaseHitCount();

        return EventNoticeResponse.from(eventNotice, eventNotice.getFiles());
    }

    /**
     * Update a corresponding event notice
     *
     * @param eventNoticeId ID of the event notice
     * @param request       Event Notice Request DTO
     * @return Event Notice Response DTO
     */
    // TODO: Admin check
    public EventNoticeResponse updateEventNotice(Long eventNoticeId, EventNoticeRequest request) {
        EventNotice eventNotice = eventNoticeRepository.findById(eventNoticeId).orElseThrow(() ->
                new BadRequestException(ExceptionCode.EVENT_NOTICE_NOT_FOUND));
        eventNotice.updateEventNotice(
                request.getTitle(),
                request.getContent(),
                request.isFixed());

        List<File> attachedFiles = getAttachedFiles(request.getFileIds());
        updateEventNoticeFiles(eventNotice, attachedFiles);

        return EventNoticeResponse.from(eventNotice, attachedFiles);
    }

    /**
     * Delete a corresponding event notice
     *
     * @param eventNoticeId ID of the event notice
     */
    // TODO: Admin check
    public void deleteEventNotice(Long eventNoticeId) {
        EventNotice eventNotice = eventNoticeRepository.findById(eventNoticeId).orElseThrow(() ->
                new BadRequestException(ExceptionCode.EVENT_NOTICE_NOT_FOUND));
        eventNoticeRepository.delete(eventNotice);
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
    private void updateEventNoticeFiles(EventNotice eventNotice, List<File> attachedFiles) {
        List<File> currentFiles = eventNotice.getFiles();
        List<File> filesToRemove = currentFiles.stream()
                .filter(file -> !attachedFiles.contains(file))
                .toList();
        filesToRemove.forEach(file -> {
            currentFiles.remove(file);
            fileRepository.delete(file);
        });
        attachedFiles.forEach(file -> {
            if (!currentFiles.contains(file)) {
                file.setEventNotice(eventNotice);
                currentFiles.add(file);
            }
        });
    }
    
}