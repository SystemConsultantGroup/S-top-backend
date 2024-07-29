package com.scg.stop.domain.event.service;


import com.scg.stop.domain.event.domain.EventNotice;
import com.scg.stop.domain.event.dto.request.EventNoticeRequest;
import com.scg.stop.domain.event.dto.response.EventNoticeListElementResponse;
import com.scg.stop.domain.event.dto.response.EventNoticeResponse;
import com.scg.stop.domain.event.repository.EventNoticeRepository;
import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.file.dto.response.FileResponse;
import com.scg.stop.domain.file.repository.FileRepository;
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
        List<File> attachedFiles = new ArrayList<>();
        if (request.getFileIds() != null && !request.getFileIds().isEmpty()) {
            attachedFiles = fileRepository.findByIdIn(request.getFileIds());
            if (attachedFiles.size() != request.getFileIds().size()) {
                throw new BadRequestException(ExceptionCode.FILE_NOT_FOUND);
            }
        }

        EventNotice newEventNotice = EventNotice.from(request.getTitle(), request.getContent(), request.isFixed(), attachedFiles);
        eventNoticeRepository.save(newEventNotice);

        List<FileResponse> files = attachedFiles.stream()
                .map(FileResponse::from)
                .collect(Collectors.toList());
        return EventNoticeResponse.from(newEventNotice, files);
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
     * @param id ID of the event notice
     * @return Event Notice Response DTO
     */
    public EventNoticeResponse getEventNotice(Long id) {
        EventNotice eventNotice = eventNoticeRepository.findById(id).orElseThrow(() ->
                new BadRequestException(ExceptionCode.EVENT_NOTICE_NOT_FOUND));
        eventNotice.increaseHitCount();

        List<FileResponse> files = eventNotice.getFiles().stream()
                .map(FileResponse::from)
                .collect(Collectors.toList());
        return EventNoticeResponse.from(eventNotice, files);
    }


    /**
     * Update a corresponding event notice
     *
     * @param id      ID of the event notice
     * @param request Event Notice Request DTO
     * @return Event Notice Response DTO
     */
    // TODO: Admin check
    public EventNoticeResponse updateEventNotice(Long id, EventNoticeRequest request) {
        EventNotice eventNotice = eventNoticeRepository.findById(id).orElseThrow(() ->
                new BadRequestException(ExceptionCode.EVENT_NOTICE_NOT_FOUND));
        eventNotice.updateEventNotice(request.getTitle(), request.getContent(), request.isFixed());

        List<File> attachedFiles = new ArrayList<>();
        if (request.getFileIds() != null && !request.getFileIds().isEmpty()) {
            attachedFiles = fileRepository.findByIdIn(request.getFileIds());
            if (attachedFiles.size() != request.getFileIds().size()) {
                throw new BadRequestException(ExceptionCode.FILE_NOT_FOUND);
            }
        }

        // Find files that are no longer attached
        List<File> filesToRemove = new ArrayList<>(eventNotice.getFiles());
        filesToRemove.removeAll(attachedFiles);

        // Remove files that are no longer attached from the file table
        filesToRemove.forEach(file -> {
            eventNotice.getFiles().remove(file);
            fileRepository.delete(file);
        });

        // Add new files and set noticeId
        attachedFiles.forEach(file -> {
            if (!eventNotice.getFiles().contains(file)) {
                file.setEventNotice(eventNotice);
                eventNotice.getFiles().add(file);
            }
        });

        List<FileResponse> files = eventNotice.getFiles().stream()
                .map(FileResponse::from)
                .collect(Collectors.toList());

        return EventNoticeResponse.from(eventNotice, files);
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
}
