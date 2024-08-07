package com.scg.stop.domain.event.domain;


import com.scg.stop.domain.file.domain.File;
import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class EventNotice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer hitCount = 0;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean fixed;
    
    @OneToMany(fetch = LAZY, mappedBy = "eventNotice", cascade = REMOVE, orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    private EventNotice(String title, String content, Integer hitCount, boolean fixed, List<File> files) {
        this.title = title;
        this.content = content;
        this.hitCount = hitCount;
        this.fixed = fixed;
        files.forEach(file -> file.setEventNotice(this));
    }

    public static EventNotice from(String title, String content, boolean fixed, List<File> files) {
        return new EventNotice(
                title,
                content,
                0,
                fixed,
                files
        );
    }

    public void updateEventNotice(String title, String content, boolean fixed) {
        this.title = title;
        this.content = content;
        this.fixed = fixed;
    }

    public void increaseHitCount() {
        this.hitCount++;
    }
}
