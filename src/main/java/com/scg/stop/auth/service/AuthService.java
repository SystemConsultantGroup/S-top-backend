package com.scg.stop.auth.service;

import com.scg.stop.auth.JwtUtil;
import com.scg.stop.auth.domain.RefreshToken;
import com.scg.stop.auth.domain.UserToken;
import com.scg.stop.auth.domain.request.RegisterRequest;
import com.scg.stop.auth.domain.response.RegisterResponse;
import com.scg.stop.auth.infrastructure.KakaoOAuthProvider;
import com.scg.stop.auth.infrastructure.KakaoUserInfo;
import com.scg.stop.auth.repository.RefreshTokenRepository;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.global.exception.InvalidJwtException;
import com.scg.stop.user.domain.Application;
import com.scg.stop.user.domain.Department;
import com.scg.stop.user.domain.Student;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import com.scg.stop.user.repository.ApplicationRepository;
import com.scg.stop.user.repository.DepartmentRepository;
import com.scg.stop.user.repository.StudentRepository;
import com.scg.stop.user.repository.UserRepository;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final JwtUtil jwtUtil;
    private final KakaoOAuthProvider kakaoOAuthProvider;

    public UserToken login(String accessCode) {
        String kakaoAccessToken = kakaoOAuthProvider.fetchKakaoAccessToken(accessCode);
        KakaoUserInfo userInfo = kakaoOAuthProvider.getUserInfo(kakaoAccessToken);

        User user = findOrCreateUser(userInfo.getSocialLoginId());

        UserToken userToken = jwtUtil.createLoginToken(user.getId().toString());
        RefreshToken refreshToken = new RefreshToken(userToken.getRefreshToken(), user.getId());
        refreshTokenRepository.save(refreshToken);
        return userToken;
    }

    // 카카오 & 네이버 중 하나만 회원가입 가능
    private User findOrCreateUser(String socialLoginId) {
        return userRepository.findBySocialLoginId(socialLoginId)
                .orElseGet(() -> createUser(socialLoginId));
    }

    public RegisterResponse finishRegister(User user, RegisterRequest registerRequest) {
        if (registerRequest.getUserType().equals(UserType.STUDENT)) {
            Department department = departmentRepository.findByName(
                    registerRequest.getStudentInfo().getDepartment())
                    .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_DEPARTMENT));

            Student student = Student.of(registerRequest.getStudentInfo().getStudentNumber(),
                    user
                    , department);
            studentRepository.save(student);
        }
        else if (Arrays.asList(UserType.INACTIVE_PROFESSOR, UserType.COMPANY, UserType.INACTIVE_COMPANY, UserType.PROFESSOR)
                .contains(registerRequest.getUserType())) {
            Application application = new Application(registerRequest.getDivision(), registerRequest.getPosition(),
                    user);
            applicationRepository.save(application);
        }
        user.register(registerRequest.getName(),
                registerRequest.getEmail(),
                registerRequest.getPhoneNumber(),
                registerRequest.getUserType(),
                registerRequest.getSignUpSource());
        userRepository.save(user);
        return RegisterResponse.from(user);
    }
    private User createUser(String socialLoginId) {
        return userRepository.save(new User(socialLoginId));
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
