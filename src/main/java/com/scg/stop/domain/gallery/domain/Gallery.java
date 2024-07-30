package com.scg.stop.domain.gallery.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.gallery.dto.request.CreateGalleryRequest;
import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    @OneToMany(fetch = LAZY, mappedBy = "gallery")
    private List<File> files = new ArrayList<>();

    private Gallery(String title, String content, Integer year, Integer month, List<File> files) {
        this.title = title;
        this.content = content;
        this.year = year;
        this.month = month;
        this.files = files;
        files.forEach(file -> file.setGallery(this));
    }

    public static Gallery of(String title, String content, Integer year, Integer month, List<File> files) {
        return new Gallery(title, content, year, month, files);
    }

    public void update(String title, String content, Integer year, Integer month, List<File> files) {
        this.title = title;
        this.content = content;
        this.year = year;
        this.month = month;
        this.files.clear();
        this.files.addAll(files);
        files.forEach(file -> file.setGallery(this));
    }
}
