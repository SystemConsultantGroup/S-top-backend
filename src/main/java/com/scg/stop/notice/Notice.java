package com.scg.stop.notice;

import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.BaseNoticeEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Notice extends BaseNoticeEntity {
}
