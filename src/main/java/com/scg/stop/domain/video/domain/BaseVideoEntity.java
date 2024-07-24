package com.scg.stop.domain.video.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.scg.stop.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public class BaseVideoEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String youtubeId;

    @Column(nullable = false)
    private Integer year;

    public void updateBaseVideoEntity(String title, String youtubeId, Integer year) {
        this.title = title;
        this.youtubeId = youtubeId;
        this.year = year;
    }

}
