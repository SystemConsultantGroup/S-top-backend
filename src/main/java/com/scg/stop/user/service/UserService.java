package com.scg.stop.user.service;

import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.user.domain.Department;
import com.scg.stop.user.domain.Student;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import com.scg.stop.user.dto.request.UserUpdateRequest;
import com.scg.stop.user.dto.response.UserResponse;
import com.scg.stop.user.repository.DepartmentRepository;
import com.scg.stop.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public UserResponse getMe(User user) {
        if (user.getUserType().equals(UserType.STUDENT)) {
            return UserResponse.of(
                    user,
                    null,
                    null,
                    user.getStudentInfo().getStudentNumber(),
                    user.getStudentInfo().getDepartment().getName()
            );
        }
        else if (Arrays.asList(UserType.INACTIVE_PROFESSOR, UserType.COMPANY, UserType.INACTIVE_COMPANY, UserType.PROFESSOR).contains(user.getUserType())) {
            return UserResponse.of(
                    user,
                    user.getApplication().getDivision(),
                    user.getApplication().getPosition(),
                    null,
                    null
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
        if (request.getUserType() != user.getUserType()) {
            throw new BadRequestException(ExceptionCode.UNABLE_TO_EDIT_USER_TYPE);
        }

        if (request.getName() != null) user.updateName(request.getName());
        if (request.getPhone() != null) user.updatePhone(request.getPhone());
        if (request.getEmail() != null) user.updateEmail(request.getEmail());

        if (user.getUserType().equals(UserType.STUDENT)) {
            if (request.getStudentNumber() != null) user.getStudentInfo().updateStudentNumber(request.getStudentNumber());
            if (request.getDepartmentName() != null) user.getStudentInfo().getDepartment().updateName(request.getDepartmentName());
        }

        if (Arrays.asList(UserType.INACTIVE_PROFESSOR, UserType.COMPANY, UserType.INACTIVE_COMPANY, UserType.PROFESSOR).contains(user.getUserType())) {
            if (request.getDivision() != null) user.getApplication().updateDivision(request.getDivision());
            if (request.getPosition() != null) user.getApplication().updatePosition(request.getPosition());
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
