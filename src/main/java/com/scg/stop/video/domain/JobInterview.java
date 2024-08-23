package com.scg.stop.video.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class JobInterview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String youtubeId;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private String talkerBelonging;

    @Column(nullable = false)
    private String talkerName;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private JobInterviewCategory category;

    @OneToMany(fetch = LAZY, mappedBy = "jobInterview", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FavoriteVideo> favoriteVideos;

    private JobInterview(String title, String youtubeId, Integer year, String talkerBelonging, String talkerName, JobInterviewCategory category) {
        this.title = title;
        this.youtubeId = youtubeId;
        this.year = year;
        this.talkerBelonging = talkerBelonging;
        this.talkerName = talkerName;
        this.category = category;
        this.favoriteVideos = new ArrayList<>();
    }

    public static JobInterview from(
            String title,
            String youtubeId,
            Integer year,
            String talkerBelonging,
            String talkerName,
            JobInterviewCategory category
    ) {
        return new JobInterview(
                title,
                youtubeId,
                year,
                talkerBelonging,
                talkerName,
                category
        );
    }

    public void updateJobInterview(
            String title,
            String youtubeId,
            Integer year,
            String talkerBelonging,
            String talkerName,
            JobInterviewCategory category
    ) {
        this.title = title;
        this.youtubeId = youtubeId;
        this.year = year;
        this.talkerBelonging = talkerBelonging;
        this.talkerName = talkerName;
        this.category = category;
    }

    public void addFavoriteVideo(FavoriteVideo favoriteVideo) {
        this.favoriteVideos.add(favoriteVideo);
    }

    public void removeFavoriteVideo(FavoriteVideo favoriteVideo) {
        this.favoriteVideos.remove(favoriteVideo);
    }
}
