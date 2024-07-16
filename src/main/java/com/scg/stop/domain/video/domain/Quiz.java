package com.scg.stop.domain.video.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Quiz {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Type(JsonType.class)
    @Column(nullable = false, columnDefinition = "TEXT")
    private Map<String, QuizInfo> quiz;
}