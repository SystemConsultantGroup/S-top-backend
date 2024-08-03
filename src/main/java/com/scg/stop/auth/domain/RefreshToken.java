package com.scg.stop.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


// TODO : Redis Migration
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private String token;

    @Column(nullable = false)
    private Long userId;

    public RefreshToken(final String token, final Long userId) {
        this.token = token;
        this.userId = userId;
    }
}
