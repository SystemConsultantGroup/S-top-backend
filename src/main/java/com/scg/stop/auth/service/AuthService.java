package com.scg.stop.auth.service;

import com.scg.stop.auth.JwtUtil;
import com.scg.stop.auth.domain.RefreshToken;
import com.scg.stop.auth.domain.UserToken;
import com.scg.stop.auth.domain.request.LoginRequest;
import com.scg.stop.auth.infrastructure.KakaoOAuthProvider;
import com.scg.stop.auth.infrastructure.KakaoUserInfo;
import com.scg.stop.auth.repository.RefreshTokenRepository;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.global.exception.InvalidJwtException;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final KakaoOAuthProvider kakaoOAuthProvider;

    public UserToken login(LoginRequest loginRequest) {
        String kakaoAccessToken = kakaoOAuthProvider.fetchKakaoAccessToken(loginRequest.getCode());
        KakaoUserInfo userInfo = kakaoOAuthProvider.getUserInfo(kakaoAccessToken);

        User user = findOrCreateUser(userInfo.getSocialLoginId(), userInfo.getNickname());

        UserToken userToken = jwtUtil.createLoginToken(user.getId().toString());
        RefreshToken refreshToken = new RefreshToken(userToken.getRefreshToken(), user.getId());
        refreshTokenRepository.save(refreshToken);
        return userToken;
    }

    // TODO: 카카오 & 네이버 둘다 회원가능 가능?? 하나만?
    private User findOrCreateUser(String socialLoginId, String nickname) {
        return userRepository.findBySocialLoginId(socialLoginId)
                .orElseGet(() -> createUser(socialLoginId, nickname, );
    }

    private User createUser(String socialLoginId, String nickname, String profileImageUrl) {
        return userRepository.save(new User(socialLoginId, ));
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }

    public String reissueAccessToken(String refreshToken, String authHeader) {
        //Bearer 제거
        String accessToken = authHeader.split(" ")[1];

        //토큰 만료, 비밀키 무결성 검사
        jwtUtil.validateRefreshToken(refreshToken);

        //Access Token 이 유효한 경우 -> 재반환
        if (jwtUtil.isAccessTokenValid(accessToken)) {
            return accessToken;
        }

        //Access Token 이 만료된 경우 -> Refresh Token DB 검증 후 재발급
        if (jwtUtil.isAccessTokenExpired(accessToken)) {
            RefreshToken foundRefreshToken = refreshTokenRepository.findById(refreshToken)
                    .orElseThrow(() -> new InvalidJwtException(ExceptionCode.INVALID_REFRESH_TOKEN));
            return jwtUtil.reissueAccessToken(foundRefreshToken.getUserId().toString());
        }
        throw new InvalidJwtException(ExceptionCode.FAILED_TO_VALIDATE_TOKEN);
    }
}
