package com.scg.stop.domain.notice.domain.service;

import com.scg.stop.domain.notice.domain.Notice;
import com.scg.stop.domain.notice.domain.dto.NoticeDto;
import com.scg.stop.domain.notice.domain.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    @Transactional
    public Long createNotice(NoticeDto.Request dto) {
        Notice newNotice = dto.toEntity();
        noticeRepository.save(newNotice);
        return newNotice.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notice> getNoticeList() {
        return noticeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeDto.Response getNotice(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("No corresponding notice found."));
        return new NoticeDto.Response(notice);
    }

    @Override
    @Transactional
    public void updateNotice(Long id, NoticeDto.Request dto) {
        Notice notice = noticeRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("No corresponding notice found."));
        notice.update(dto.getTitle(), dto.getContent(), dto.isFixed());
    }

    @Override
    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                new IllegalArgumentException("No corresponding notice found."));
        noticeRepository.delete(notice);
    }
}
