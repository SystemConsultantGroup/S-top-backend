package com.scg.stop.domain.video.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.video.dto.request.JobInterviewRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class JobInterview extends BaseVideoEntity {

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private JobInterviewCategory category;

    @OneToMany(fetch = LAZY, mappedBy = "jobInterview")
    private List<FavoriteVideo> favoriteVideos;

    public static JobInterview createJobInterview(String title, String youtubeId, Integer year, JobInterviewCategory category) {
        JobInterview interview = new JobInterview();
        interview.setTitle(title);
        interview.setCategory(category);
        interview.setYear(year);
        interview.setYoutubeId(youtubeId);
        return interview;
    }
}
