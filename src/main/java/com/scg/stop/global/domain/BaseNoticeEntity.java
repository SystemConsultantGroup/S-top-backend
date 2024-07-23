package com.scg.stop.global.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@MappedSuperclass
public abstract class BaseNoticeEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer hitCount = 0;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean fixed;

//    // protected constructor for static factory method
//    protected BaseNoticeEntity(String title, String content, boolean fixed, Integer hitCount) {
//        this.title = title;
//        this.content = content;
//        this.fixed = fixed;
//        this.hitCount = hitCount;
//    }
}
