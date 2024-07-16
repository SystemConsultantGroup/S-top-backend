package com.scg.stop.interview;

import static jakarta.persistence.GenerationType.IDENTITY;
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
