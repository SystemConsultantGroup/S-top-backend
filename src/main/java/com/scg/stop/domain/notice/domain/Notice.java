package com.scg.stop.domain.notice.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.file.domain.File;
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

    @OneToMany(fetch = LAZY, mappedBy = "notice")
    private List<File> files = new ArrayList<>();

    // constructor for static method
    private Notice(String title, String content, boolean fixed, Integer hitCount) {
        super(title, content, fixed, hitCount);
    }

    // static method for creating instance using constructor
    public static Notice createNotice(String title, String content, boolean fixed) {
        return new Notice(title, content, fixed, 0);
    }
}
