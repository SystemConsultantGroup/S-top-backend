package com.scg.stop.domain.project.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
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
    private String videoUrl;

    @Column(nullable = false)
    private String techStack;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private AwardStatus awardStatus = AwardStatus.NONE;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "thumbnail_id")
    private File thumbnail;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "poster_id")
    private File poster;

    @OneToMany(mappedBy = "project")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<FavoriteProject> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Inquiry> inquiries = new ArrayList<>();
}
