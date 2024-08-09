package com.scg.stop.domain.file.domain;

import com.scg.stop.domain.gallery.domain.Gallery;
import com.scg.stop.event.domain.EventNotice;
import com.scg.stop.global.domain.BaseTimeEntity;
import com.scg.stop.notice.domain.Notice;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String mimeType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "event_notice_id")
    private EventNotice eventNotice;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public void setEventNotice(EventNotice eventNotice) {
        this.eventNotice = eventNotice;
    }
}