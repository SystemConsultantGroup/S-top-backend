package com.scg.stop.video.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.global.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Quiz extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Type(JsonType.class)
    @Column(nullable = false, columnDefinition = "TEXT")
    private Map<String, QuizInfo> quiz;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "talk_id")
    @Setter
    private Talk talk;

    @OneToMany(fetch = LAZY, mappedBy = "quiz")
    private List<UserQuiz> userQuizzes = new ArrayList<>();

    public Quiz(
            Map<String, QuizInfo> quiz
    ) {
        this.quiz = quiz;
    }

    public static Quiz from(Map<String, QuizInfo> quiz) {
        return new Quiz(
                quiz
        );
    }

    public void updateQuiz(Map<String, QuizInfo> quiz) {
        this.quiz = quiz;
    }
}