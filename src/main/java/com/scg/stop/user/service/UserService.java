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

import static io.micrometer.common.util.StringUtils.isBlank;
import static io.micrometer.common.util.StringUtils.isNotBlank;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public UserResponse getMe(User user) {
        if (user.getUserType().equals(UserType.STUDENT)) {
            Student studentInfo = user.getStudentInfo();
            Department department = departmentRepository.findById(studentInfo.getDepartment().getId())
                    .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_DEPARTMENT));

            return UserResponse.of(
                    user,
                    null,
                    null,
                    studentInfo.getStudentNumber(),
                    department.getName()
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
        user.updateName(request.getName());
        user.updatePhone(request.getPhoneNumber());
        user.updateEmail(request.getEmail());

        if (user.getUserType().equals(UserType.STUDENT)) {
            request.validateStudentInfo();

            Department department = departmentRepository.findByName(
                            request.getDepartment())
                    .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_DEPARTMENT));

            user.getStudentInfo().updateStudentNumber(request.getStudentNumber());
            user.getStudentInfo().updateDepartment(department);
        }
        else if (Arrays.asList(UserType.INACTIVE_PROFESSOR, UserType.COMPANY, UserType.INACTIVE_COMPANY, UserType.PROFESSOR).contains(user.getUserType())) {
            if (isNull(request.getDivision()) || isNull(request.getPosition())) {
                throw new BadRequestException(ExceptionCode.DIVISION_OR_POSITION_REQUIRED);
            }

            user.getApplication().updateDivision(request.getDivision());
            user.getApplication().updatePosition(request.getPosition());
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
