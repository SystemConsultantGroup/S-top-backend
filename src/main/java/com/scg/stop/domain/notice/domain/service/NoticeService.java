package com.scg.stop.domain.notice.domain.service;

import com.scg.stop.domain.notice.domain.Notice;
import com.scg.stop.domain.notice.domain.dto.NoticeDto;

import java.util.List;

public interface NoticeService {

    /**
     * 공지 사항 등록
     * @param dto notice request dto
     * @return notice id (pk)
     */
    public Long createNotice(NoticeDto.Request dto);


    /**
     * 공지 사항 리스트 조회
     * @return notice list
     */
    public List<Notice> getNoticeList();

    /**
     * 개별 공지 사항 상세 조회
     * @param id notice id (pk)
     * @return notice response dto
     */
    public NoticeDto.Response getNotice(Long id);

    /**
     * 공지 사항 수정
     * @param id notice id (pk)
     * @param dto notice request dto
     */
    public void updateNotice(Long id, NoticeDto.Request dto);

    /**
     * 공지 사항 삭제
     * @param id notice id (pk)
     */
    public void deleteNotice(Long id);
}
