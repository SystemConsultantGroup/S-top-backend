package com.scg.stop.video.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.user.domain.User;
import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class UserQuiz extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer tryCount = 1;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isSuccess = false;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    private UserQuiz(User user, Quiz quiz, boolean isSuccess) {
        this.user = user;
        this.quiz = quiz;
        this.isSuccess = isSuccess;
    }

    public static UserQuiz from(User user, Quiz quiz, boolean isSuccess) {
        return new UserQuiz(user, quiz, isSuccess);
    }

    public void updateSuccess(boolean success) {
        isSuccess = success;
        tryCount = tryCount + 1;
    }
}
