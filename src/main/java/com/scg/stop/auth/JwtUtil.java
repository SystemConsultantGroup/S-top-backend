package com.scg.stop.auth;

import com.scg.stop.auth.domain.UserToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessTokenExpiry;
    private final Long refreshTokenExpiry;


    public JwtUtil(
            @Value("${spring.auth.jwt.secret-key}") final String secretKey,
            @Value("${spring.auth.jwt.access-token-expiry}") final Long accessTokenExpiry,
            @Value("${spring.auth.jwt.refresh-token-expiry}") final Long refreshTokenExpiry
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiry = accessTokenExpiry;
        this.refreshTokenExpiry = refreshTokenExpiry;
    }

    // 토큰 생성 //
    public UserToken createLoginToken(String subject) {
        String refreshToken = createToken("",refreshTokenExpiry);
        String accessToken = createToken(subject, accessTokenExpiry);
        return new UserToken(accessToken, refreshToken);
    }
    public String createToken(String subject, Long expiredMs) {
        final Date now = new Date();
        final Date expiredDate = new Date(now.getTime() + expiredMs);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }

    public String reissueAccessToken(String subject) {
        return createToken(subject, accessTokenExpiry);
    }

    // 토근 정보 추출 //
    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }
    public String getSubject(String token) {
        return parseToken(token)
                .getBody().getSubject();
    }

    // 유효성 검사 //
    public void validateRefreshToken(String refreshToken) {
        try {
            parseToken(refreshToken);
        } catch (JwtException e) {
            throw new InvalidJwtException();
        }
    }

    public boolean isAccessTokenValid(String accessToken) {
        try {
            parseToken(accessToken);
        }catch (JwtException e) {
            return false;
        }
        return true;
    }

    public boolean isAccessTokenExpired(String accessToken) {
        try {
            parseToken(accessToken);
        } catch (ExpiredJwtException e){
            return true;
        }
        return false;
    }
}
