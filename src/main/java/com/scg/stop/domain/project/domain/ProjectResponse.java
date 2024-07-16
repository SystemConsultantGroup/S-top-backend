package com.scg.stop.domain.project.domain;

import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.global.domain.BaseResponseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ProjectResponse extends BaseResponseEntity {
}
