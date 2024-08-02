package com.scg.stop.domain.gallery.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Gallery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer hitCount = 0;

    @OneToMany(fetch = LAZY, mappedBy = "gallery", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    private Gallery(String title, Integer year, Integer month, List<File> files) {
        this.title = title;
        this.year = year;
        this.month = month;
        this.files = files;
        files.forEach(file -> file.setGallery(this));
    }

    public static Gallery of(String title, Integer year, Integer month, List<File> files) {
        return new Gallery(title, year, month, files);
    }

    public void update(String title, Integer year, Integer month, List<File> files) {
        this.title = title;
        this.year = year;
        this.month = month;
        this.files.clear();
        this.files.addAll(files);
        files.forEach(file -> file.setGallery(this));
    }

    public void increaseHitCount() {
        this.hitCount += 1;
    }
}
