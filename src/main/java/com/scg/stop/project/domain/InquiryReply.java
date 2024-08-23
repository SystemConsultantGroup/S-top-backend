package com.scg.stop.project.domain;

import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class InquiryReply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;

    private InquiryReply(String title, String content, Inquiry inquiry) {
        this.title = title;
        this.content = content;
        this.inquiry = inquiry;
    }

    public static InquiryReply createInquiryReply(String title, String content, Inquiry inquiry) {
        return new InquiryReply(title, content, inquiry);
    }

    public void updateInquiryResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
