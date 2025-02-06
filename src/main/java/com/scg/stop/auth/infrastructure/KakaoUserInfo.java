package com.scg.stop.auth.infrastructure;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class KakaoUserInfo {

    @Getter
    @JsonProperty("id")
    private String socialLoginId;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    private static class KakaoAccount {
        @JsonProperty("profile")
        private KakaoProfile kakaoProfile;
    }

    private static class KakaoProfile {}
}
