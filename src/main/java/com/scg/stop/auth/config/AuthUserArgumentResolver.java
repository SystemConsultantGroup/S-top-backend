package com.scg.stop.auth.config;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.auth.JwtUtil;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.global.exception.InvalidJwtException;
import com.scg.stop.global.exception.UnauthorizedException;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import com.scg.stop.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
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
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)  {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null) {
            throw new InvalidJwtException(ExceptionCode.FAILED_TO_VALIDATE_TOKEN);
        }

        AccessType[] allowedTypes = Objects.requireNonNull(parameter.getParameterAnnotation(AuthUser.class)).accessType();
        List<AccessType> accessTypeList = Arrays.asList(allowedTypes);

        if(accessTypeList.contains(AccessType.OPTIONAL) && !hasAccessToken(request))
        {
            return null;
        }

        String contextPath = request.getRequestURI();
        String accessToken = extractAccessToken(request);
        String refreshToken = extractRefreshToken(request);

        //검증
        if (jwtUtil.isAccessTokenValid(accessToken)) {
            User extractedUser = extractUser(accessToken);
            UserType extractedUserType = extractUser(accessToken).getUserType();

            // TEMP USER 이고 REGISTER PATH 로 요청이면 REGISTER 페이지로 안내
            // 그 외 TEMP USER 는 전부 예외 처리
            String registerPath = "/auth/register";
            if (extractedUserType.equals(UserType.TEMP) ) {
                if (contextPath.equals(registerPath)) {
                    return extractedUser;
                }
                else {
                    throw new BadRequestException(ExceptionCode.REGISTER_NOT_FINISHED);
                }
            }


            if (accessTypeList.contains(AccessType.ALL) || accessTypeList.contains(AccessType.OPTIONAL)) {
                return extractedUser;
            }
            else if (extractedUserType.equals(UserType.ADMIN)) {
                if (accessTypeList.contains(AccessType.ADMIN)) return extractedUser;
            }
            else if (extractedUserType.equals(UserType.COMPANY) || extractedUserType.equals(UserType.INACTIVE_COMPANY)){
                if (accessTypeList.contains(AccessType.COMPANY)) return extractedUser;
            }
            else if (extractedUserType.equals(UserType.PROFESSOR) || extractedUserType.equals(UserType.INACTIVE_PROFESSOR)) {
                if (accessTypeList.contains(AccessType.PROFESSOR)) return extractedUser;
            }
            else if (extractedUserType.equals(UserType.STUDENT) || extractedUserType.equals(UserType.OTHERS)) {
                if (accessTypeList.contains(AccessType.STUDENT)) return extractedUser;
            }
        }

        throw new UnauthorizedException(ExceptionCode.NOT_AUTHORIZED);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUser.class);
    }

    private String extractAccessToken(HttpServletRequest request) {
        final String BEARER = "Bearer ";
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            throw new InvalidJwtException(ExceptionCode.INVALID_ACCESS_TOKEN);
        }
        return authHeader.substring(BEARER.length()).trim();
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

    private boolean hasAccessToken(HttpServletRequest request) {
        final String BEARER = "Bearer ";
        final String BEARER_NULL = "Bearer null";
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return authHeader != null && authHeader.startsWith(BEARER) && !authHeader.equalsIgnoreCase(BEARER_NULL);
    }
}
