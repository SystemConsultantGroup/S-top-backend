package com.scg.stop.user.service;

import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import com.scg.stop.user.dto.request.UserUpdateRequest;
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

    public UserResponse updateMe(User user, UserUpdateRequest request) {
        if (request.getName() != null) user.updateName(request.getName());
        if (request.getPhone() != null) user.updatePhone(request.getPhone());
        if (request.getEmail() != null) user.updateEmail(request.getEmail());

        if (user.getUserType() == UserType.PROFESSOR || user.getUserType() == UserType.COMPANY) {
            if (request.getDivision() != null) user.getApplication().updateDivision(request.getDivision());
            if (request.getPosition() != null) user.getApplication().updatePosition(request.getPosition());
        }

        if (user.getUserType() == UserType.STUDENT) {
            if (request.getStudentNumber() != null) user.getStudentInfo().updateStudentNumber(request.getStudentNumber());
            if (request.getDepartmentName() != null) user.getStudentInfo().getDepartment().updateName(request.getDepartmentName());
        }

        userRepository.save(user);

        return UserResponse.of(
                user,
                user.getApplication() != null ? user.getApplication().getDivision() : null,
                user.getApplication() != null ? user.getApplication().getPosition() : null,
                user.getStudentInfo() != null ? user.getStudentInfo().getStudentNumber() : null,
                user.getStudentInfo() != null ? user.getStudentInfo().getDepartment().getName() : null
        );
    }

    public void deleteMe(User user) {
        userRepository.delete(user);
    }
}
