package com.scg.stop.domain.video.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Talk extends BaseVideoEntity {

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean hasQuiz;
}
