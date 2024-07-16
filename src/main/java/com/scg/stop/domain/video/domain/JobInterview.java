package com.scg.stop.domain.video.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class JobInterview extends BaseVideoEntity {
}
