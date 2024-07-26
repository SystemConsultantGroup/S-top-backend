package com.scg.stop.auth.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.global.exception.SocialLoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class KakaoOAuthProvider {
    private final RestTemplate restTemplate;
    private final String clientId;
//    private final String clientSecret;
    private final String redirectUri;
    private final String tokenUri;
    private final String userInfoUri;

    public KakaoOAuthProvider(
            RestTemplate restTemplate,
            @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
//            @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String clientSecret,
            @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirectUri,
            @Value("${spring.security.oauth2.client.provider.kakao.token-uri}") String tokenUri,
            @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}") String userInfoUri) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
//        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
        this.userInfoUri = userInfoUri;
    }
    public KakaoUserInfo getUserInfo(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, Boolean> params = new HashMap<>();
        params.put("secure_resource", true);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                requestEntity,
                KakaoUserInfo.class,
                params
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }

        throw new SocialLoginException(ExceptionCode.UNABLE_TO_GET_USER_INFO);
    }

    public String fetchKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
//        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                requestEntity,
                KakaoTokenResponse.class
        );

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new SocialLoginException(ExceptionCode.UNABLE_TO_GET_ACCESS_TOKEN))
                .getAccessToken();
    }



    @Getter
    public static class KakaoTokenResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("expires_in")
        private Integer expiresIn;
    }
}
