package com.scg.stop.video.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
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

    @Column(nullable = false)
    private String talkerBelonging;

    @Column(nullable = false)
    private String talkerName;

    @OneToOne(fetch = LAZY, mappedBy = "talk", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Quiz quiz;

    @OneToMany(fetch = LAZY, mappedBy = "talk", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FavoriteVideo> favoriteVideos;

    public Talk(String title, String youtubeId, Integer year, String talkerBelonging, String talkerName) {
        this.title = title;
        this.youtubeId = youtubeId;
        this.year = year;
        this.talkerBelonging = talkerBelonging;
        this.talkerName = talkerName;
        this.favoriteVideos = new ArrayList<>();
    }
    public static Talk from(
            String title,
            String youtubeId,
            Integer year,
            String talkerBelonging,
            String talkerName
    ) {
        return new Talk(
                title,
                youtubeId,
                year,
                talkerBelonging,
                talkerName
        );
    }

    public void updateTalk(
            String title,
            String youtubeId,
            Integer year,
            String talkerBelonging,
            String talkerName
    ) {
        this.title= title;
        this.youtubeId = youtubeId;
        this.year = year;
        this.talkerBelonging = talkerBelonging;
        this.talkerName = talkerName;
    }

    public void setQuiz(Quiz quiz) {
        if(this.quiz != null) {
            this.quiz.setTalk(null);
        }
        this.quiz = quiz;
        if(quiz != null) {
            quiz.setTalk(this);
        }
    }

    public void addFavoriteVideo(FavoriteVideo favoriteVideo) {
        this.favoriteVideos.add(favoriteVideo);
    }

    public void removeFavoriteVideo(FavoriteVideo favoriteVideo) {
        this.favoriteVideos.remove(favoriteVideo);
    }

}
