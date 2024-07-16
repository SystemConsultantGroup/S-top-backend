package com.scg.stop.project;

import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.BaseResponseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ProjectResponse extends BaseResponseEntity {
}
