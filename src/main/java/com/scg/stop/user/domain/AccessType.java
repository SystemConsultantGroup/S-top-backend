package com.scg.stop.user.domain;

import lombok.Getter;

@Getter
public enum AccessType {
    ADMIN,
    PROFESSOR,
    STUDENT,
    COMPANY,
    ALL
}
