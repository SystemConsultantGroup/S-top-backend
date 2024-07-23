package com.scg.stop.domain.video.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.video.dto.request.JobInterviewRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
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
public class JobInterview extends BaseVideoEntity {

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private JobInterviewCategory category;

    @OneToMany(fetch = LAZY, mappedBy = "jobInterview")
    private List<FavoriteVideo> favoriteVideos;

    private JobInterview(String title, String youtubeId, Integer year, JobInterviewCategory category) {
        super(null, title, youtubeId, year);
        this.category = category;
    }

    public static JobInterview from(JobInterviewRequest request) {
        return new JobInterview(
                request.getTitle(),
                request.getYoutubeId(),
                request.getYear(),
                request.getCategory()
        );
    }
}
