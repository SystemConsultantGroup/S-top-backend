package com.scg.stop.file.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.gallery.domain.Gallery;
import com.scg.stop.notice.domain.Notice;
import com.scg.stop.event.domain.EventNotice;
import com.scg.stop.global.domain.BaseTimeEntity;
import com.scg.stop.proposal.domain.Proposal;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;
    static public File of(String uuid, String name, String mimeType) {
        File file = new File();
        file.uuid = uuid;
        file.name = name;
        file.mimeType = mimeType;
        return file;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public void setEventNotice(EventNotice eventNotice) {
        this.eventNotice = eventNotice;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }
}