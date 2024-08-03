package com.scg.stop.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserToken {
    private final String accessToken;
    private final String refreshToken;
}
