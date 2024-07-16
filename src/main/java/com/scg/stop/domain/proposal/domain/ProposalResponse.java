package com.scg.stop.domain.proposal.domain;

import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.global.domain.BaseResponseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ProposalResponse extends BaseResponseEntity {
}
