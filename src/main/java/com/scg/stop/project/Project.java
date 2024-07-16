package com.scg.stop.project;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Project {

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
}
