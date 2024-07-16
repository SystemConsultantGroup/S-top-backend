package com.scg.stop.domain.event.domain;

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
public class EventNotice extends BaseNoticeEntity {

    @OneToMany(mappedBy = "eventNotice")
    private List<File> files = new ArrayList<>();
}
