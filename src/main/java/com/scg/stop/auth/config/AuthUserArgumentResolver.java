package com.scg.stop.auth.config;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.auth.JwtUtil;
import com.scg.stop.domain.project.domain.Role;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.global.exception.InvalidJwtException;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null) {
            throw new InvalidJwtException(ExceptionCode.FAILED_TO_VALIDATE_TOKEN);
        }

        String refreshToken = extractRefreshToken(request);
        String accessToken = extractAccessToken(request);

        //검증
        if (jwtUtil.isAccessTokenValid(accessToken)) {
            User extractedUser = extractUser(accessToken);
            Role[] allowedRoles = Objects.requireNonNull(parameter.getParameterAnnotation(AuthUser.class)).roles();
            for (Role allowedRole: allowedRoles){
                if (extractedUser.getRole().equals(allowedRole)){
                    return extractedUser;
                }
            }
        }

        throw new InvalidJwtException(ExceptionCode.FAILED_TO_VALIDATE_TOKEN);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUser.class);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            throw new InvalidJwtException(ExceptionCode.INVALID_ACCESS_TOKEN);
        }
        return authHeader.split(" ")[1];
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new InvalidJwtException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refresh-token"))
                .findFirst()
                .orElseThrow(() -> new InvalidJwtException(ExceptionCode.INVALID_REFRESH_TOKEN))
                .getValue();
    }

    private User extractUser(String accessToken) {
        Long userId = Long.valueOf(jwtUtil.getSubject(accessToken));

        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_USER_ID));
    }
}
