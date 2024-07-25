package com.scg.stop.domain.file.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.event.domain.EventNotice;
import com.scg.stop.domain.gallery.domain.Gallery;
import com.scg.stop.domain.notice.domain.Notice;
import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }
}
