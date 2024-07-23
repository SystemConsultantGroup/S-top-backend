package com.scg.stop.domain.notice.service;

import com.scg.stop.domain.notice.domain.Notice;
import com.scg.stop.domain.notice.dto.request.NoticeRequestDto;
import com.scg.stop.domain.notice.dto.response.NoticeResponseDto;
import com.scg.stop.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    /**
     * Create a new notice
     * @param dto Notice Request DTO
     * @return ID of the created notice
     */
    // TODO: Admin check
    public NoticeResponseDto createNotice(NoticeRequestDto dto) {
        Notice newNotice = dto.toEntity();
        noticeRepository.save(newNotice);
        return NoticeResponseDto.from(newNotice);
    }

    /**
     * Get a list of notices
     * @return List of notices
     */
    @Transactional(readOnly = true)
    public Page<NoticeResponseDto> getNoticeList(String title, Pageable pageable) {
        return noticeRepository.findNotices(title, pageable);
    }

    /**
     * Get a corresponding notice
     * @param id ID of the notice
     * @return Notice Response DTO
     */
    public NoticeResponseDto getNotice(Long id) {
        // TODO: do i need to check if the notice exists when operating increaseHitCount?
        noticeRepository.increaseHitCount(id);
        Notice notice = noticeRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("요청한 ID에 해당하는 공지사항이 존재하지 않습니다."));
        return NoticeResponseDto.from(notice);
    }

    /**
     * Update a corresponding notice
     * @param id ID of the notice
     * @param dto Notice Request DTO
     */
    // TODO: Admin check
    public NoticeResponseDto updateNotice(Long id, NoticeRequestDto dto) {
        int isUpdateSuccess = noticeRepository.updateNotice(id, dto);
        if (isUpdateSuccess < 1) {
            throw new IllegalArgumentException("요청한 ID에 해당하는 공지사항이 존재하지 않습니다.");
        }
        Notice notice = noticeRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("요청한 ID에 해당하는 공지사항이 존재하지 않습니다."));
        return NoticeResponseDto.from(notice);
    }

    /**
     * Delete a corresponding notice
     * @param noticeId ID of the notice
     */
    // TODO: Admin check
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                new IllegalArgumentException("요청한 ID에 해당하는 공지사항이 존재하지 않습니다."));
        noticeRepository.delete(notice);
    }
}