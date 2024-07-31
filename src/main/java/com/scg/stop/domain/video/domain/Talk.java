package com.scg.stop.domain.video.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.video.dto.request.TalkRequest;
import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Talk extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String youtubeId;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean hasQuiz;

    @OneToOne(fetch = LAZY, mappedBy = "talk")
    private Quiz quiz;

    @OneToMany(fetch = LAZY, mappedBy = "talk")
    private List<FavoriteVideo> favoriteVideos;

    public Talk(String title, String youtubeId, Integer year, boolean hasQuiz) {
        this.title = title;
        this.youtubeId = youtubeId;
        this.year = year;
        this.hasQuiz = hasQuiz;
    }
    public static Talk from(TalkRequest request) {
        return new Talk(
                request.getTitle(),
                request.getYoutubeId(),
                request.getYear(),
                request.isHasQuiz()
        );
    }
}
