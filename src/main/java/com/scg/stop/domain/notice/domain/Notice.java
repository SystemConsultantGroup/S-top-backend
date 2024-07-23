package com.scg.stop.domain.notice.domain;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.notice.dto.request.NoticeRequestDto;
import com.scg.stop.global.domain.BaseNoticeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Notice extends BaseNoticeEntity {

    @OneToMany(fetch = LAZY, mappedBy = "notice", cascade = REMOVE, orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    // constructor for static method
    // TODO: hanlde attached files
    private Notice(String title, String content, Integer hitCount, boolean fixed) {
        super(null, title, content, hitCount, fixed);
    }

    // static method for creating new notice entity
    // TODO: hanlde attached files
    public static Notice from(NoticeRequestDto requestDto) {
        return new Notice(
            requestDto.getTitle(),
            requestDto.getContent(),
            0,
            requestDto.isFixed()
        );
    }
}
