package com.scg.stop.domain.event.domain;

import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.global.domain.BaseNoticeEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class EventNotice extends BaseNoticeEntity {
}
