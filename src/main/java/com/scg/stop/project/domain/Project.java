package com.scg.stop.project.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.file.domain.File;
import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private ProjectType type;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private ProjectCategory category;

    @Column(nullable = false)
    private String team;

    @Column(nullable = false)
    private String youtubeId;

    @Column(nullable = false)
    private Integer year;

    private String url;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private AwardStatus awardStatus = AwardStatus.NONE;

    @OneToOne(fetch = LAZY, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_id")
    private File thumbnail;

    @OneToOne(fetch = LAZY, orphanRemoval = true)
    @JoinColumn(name = "poster_id")
    private File poster;

    @OneToMany(fetch = LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<FavoriteProject> favorites = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Inquiry> inquiries = new ArrayList<>();

    public void update(Project project) {
        this.name = project.getName();
        this.type = project.getType();
        this.category = project.getCategory();
        this.team = project.getTeam();
        this.youtubeId = project.getYoutubeId();
        this.year = project.getYear();
        this.url = project.getUrl();
        this.description = project.getDescription();
        this.awardStatus = project.getAwardStatus();
        this.thumbnail = project.getThumbnail();
        this.poster = project.getPoster();
        this.members = project.getMembers();
    }

    public void addLikes(Likes likes) {
        this.likes.add(likes);
    }

    public void removeLikes(Likes likes) {
        this.likes.remove(likes);
    }

    public void addFavoriteProject(FavoriteProject favoriteProject) {
        this.favorites.add(favoriteProject);
    }

    public void removeFavoriteProject(FavoriteProject favoriteProject) {
        this.favorites.remove(favoriteProject);
    }
}
