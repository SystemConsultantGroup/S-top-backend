package com.scg.stop.auth.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.auth.domain.UserToken;
import com.scg.stop.auth.domain.request.LoginRequest;
import com.scg.stop.auth.domain.request.RegisterRequest;
import com.scg.stop.auth.domain.response.AccessTokenResponse;
import com.scg.stop.auth.service.AuthService;
import com.scg.stop.domain.project.domain.Role;
import com.scg.stop.user.domain.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Slf4j
public class AuthController {
    private static final int ONE_WEEK_SECONDS = 604800;

    private final AuthService authService;

    @PostMapping(value = "/login/kakao")
    public ResponseEntity<AccessTokenResponse> kakaoLogin(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        UserToken userTokens = authService.login(loginRequest);

        ResponseCookie cookie = ResponseCookie.from("refresh-token", userTokens.getRefreshToken())
                .maxAge(ONE_WEEK_SECONDS)
//                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .domain(".stop.scg.skku.ac.kr")  // TODO: domain 수정
                .path("/")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new AccessTokenResponse(userTokens.getAccessToken()));
    }

    //TODO: 추가 회원가입로직 작성
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody RegisterRequest registerRequest,
            @AuthUser(roles = {Role.PROFESSOR, Role.STUDENT}) User user) {
        return ResponseEntity.created();
    }

    @PostMapping("/reissue")
    public ResponseEntity<AccessTokenResponse> reissueToken(
            @CookieValue("refresh-token") String refreshToken,
            @RequestHeader("Authorization") String authHeader
    ) {
        String reissuedToken = authService.reissueAccessToken(refreshToken, authHeader);
        return ResponseEntity.ok(new AccessTokenResponse(reissuedToken));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<Void> logout(
            @CookieValue("refresh-token") String refreshToken
    ) {

        authService.logout(refreshToken);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/login/test")
    public String test(@RequestParam String code) {
        log.info("code={}", code);
        return "ok";
    }
}

