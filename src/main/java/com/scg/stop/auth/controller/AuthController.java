package com.scg.stop.auth.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.auth.domain.UserToken;
import com.scg.stop.auth.domain.request.RegisterRequest;
import com.scg.stop.auth.domain.response.AccessTokenResponse;
import com.scg.stop.auth.domain.response.RegisterResponse;
import com.scg.stop.auth.service.AuthService;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private static final int ONE_WEEK_SECONDS = 604800;

    @Value("${spring.redirectUri}")
    private String REDIRECT_URI;

    private final AuthService authService;

    @GetMapping(value = "/login/kakao")
    public ResponseEntity<AccessTokenResponse> kakaoLogin(
            @RequestParam("code") String accessCode ,
            HttpServletResponse response
    ) {
        UserToken userTokens = authService.login(accessCode);

        ResponseCookie cookie = ResponseCookie.from("refresh-token", userTokens.getRefreshToken())
                .maxAge(ONE_WEEK_SECONDS)
//                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .domain(".stop.scg.skku.ac.kr")  // TODO: domain 수정
                .path("/")
                .build();

        ResponseCookie AccessTokenCookie = ResponseCookie.from("access-token", userTokens.getAccessToken())
                .maxAge(ONE_WEEK_SECONDS)
//                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .domain(".stop.scg.skku.ac.kr")  // TODO: domain 수정
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.addHeader("Location", REDIRECT_URI);
        response.addHeader(HttpHeaders.SET_COOKIE, AccessTokenCookie.toString());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody @Valid RegisterRequest registerRequest,
            @AuthUser(accessType = {AccessType.ALL}) User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.finishRegister(user, registerRequest));

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

}

