package com.scg.stop.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.scg.stop.auth.JwtUtil;
import com.scg.stop.auth.domain.UserToken;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

public class JwtUtilTest {

    private JwtUtil jwtUtil = new JwtUtil(
            "your-test-secret-key-1234567890123456",
            60000L,
            120000L
    );
    @Test
    @DisplayName("jwt 페이로드에 유저타입이 포함되어있다.")
    void createTokenShouldIncludeUserType() {
        // Given: User 객체 생성
        User user = new User("social");
        user.register("name", "email","010", UserType.ADMIN, "source");
        // When: JWT 생성
        UserToken userToken = jwtUtil.createLoginToken("12345", user);

        // Then: Access Token에서 Claims 추출
        Jws<Claims> claimsJws = jwtUtil.parseToken(userToken.getAccessToken());
        Claims claims = claimsJws.getBody();

        // 검증: Subject와 userType 확인
        assertThat(claims.getSubject()).isEqualTo("12345");
        assertThat(claims.get("userType", String.class)).isEqualTo("ADMIN");
    }

}
