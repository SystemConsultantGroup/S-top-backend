package com.scg.stop.domain.video.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Talk extends BaseVideoEntity {

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean hasQuiz;

    @OneToOne(fetch = LAZY, mappedBy = "talk")
    private Quiz quiz;

    @OneToMany(fetch = LAZY, mappedBy = "talk")
    private List<FavoriteVideo> favoriteVideos;
}
