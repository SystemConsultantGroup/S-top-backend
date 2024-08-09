package com.scg.stop.user.service;

import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import com.scg.stop.user.dto.response.UserResponse;
import com.scg.stop.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getMe(User user) {
        UserType userType = user.getUserType();
        if (userType == UserType.PROFESSOR || userType == UserType.COMPANY) {
            return UserResponse.of(
                    user,
                    user.getApplication().getDivision(),
                    user.getApplication().getPosition(),
                    null,
                    null
            );
        }
        else if (userType == UserType.STUDENT) {
            return UserResponse.of(
                    user,
                    null,
                    null,
                    user.getStudentInfo().getStudentNumber(),
                    user.getStudentInfo().getDepartment().getName()
            );
        }
        else {
            return UserResponse.of(
                    user,
                    null,
                    null,
                    null,
                    null
            );
        }
    }
}
