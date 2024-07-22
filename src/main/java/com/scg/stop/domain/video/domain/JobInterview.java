package com.scg.stop.domain.video.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
public class JobInterview extends BaseVideoEntity {

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private JobInterviewCategory category;

    @OneToMany(fetch = LAZY, mappedBy = "jobInterview")
    private List<FavoriteVideo> favoriteVideos;
}
