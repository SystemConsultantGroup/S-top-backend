package com.scg.stop.domain.video.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.video.dto.request.JobInterviewRequest;
import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.*;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
    }

    public static JobInterview from(JobInterviewRequest request) {
        return new JobInterview(
                request.getTitle(),
                request.getYoutubeId(),
                request.getYear(),
                request.getTalkerBelonging(),
                request.getTalkerName(),
                request.getCategory()
        );
    }

    public void updateJobInterview(JobInterviewRequest request) {
        this.title = request.getTitle();
        this.youtubeId = request.getYoutubeId();
        this.year = request.getYear();
        this.talkerBelonging = request.getTalkerBelonging();
        this.talkerName = request.getTalkerName();
        this.category = request.getCategory();
    }
}
