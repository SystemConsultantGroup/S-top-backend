package com.scg.stop.user.service;

import static com.scg.stop.user.domain.UserType.*;
import static com.scg.stop.global.exception.ExceptionCode.ALREADY_VERIFIED_USER;
import static com.scg.stop.global.exception.ExceptionCode.NOT_FOUND_APPLICATION_ID;

import com.scg.stop.user.domain.Application;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import com.scg.stop.user.dto.response.ApplicationDetailResponse;
import com.scg.stop.user.dto.response.ApplicationListResponse;
import com.scg.stop.user.repository.ApplicationRepository;
import com.scg.stop.user.repository.UserRepository;
import com.scg.stop.global.exception.BadRequestException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public Page<ApplicationListResponse> getApplications(Pageable pageable) {
        List<UserType> targetUserTypes = Arrays.asList(INACTIVE_COMPANY, INACTIVE_PROFESSOR);
        Page<Application> filteredApplications = applicationRepository.findByUserTypeIn(targetUserTypes, pageable);
        return filteredApplications.map(ApplicationListResponse::from);
    }

    public ApplicationDetailResponse getApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_APPLICATION_ID));

        List<UserType> activeUserTypes = Arrays.asList(COMPANY, PROFESSOR);
        if(activeUserTypes.contains(application.getUser().getUserType())) {
            throw new BadRequestException(ALREADY_VERIFIED_USER);
        }
        return ApplicationDetailResponse.from(application);
    }

    @Transactional
    public ApplicationDetailResponse updateApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_APPLICATION_ID));

        User user = application.getUser();
        List<UserType> activeUserTypes = Arrays.asList(COMPANY, PROFESSOR);
        if(activeUserTypes.contains(user.getUserType())) {
            throw new BadRequestException(ALREADY_VERIFIED_USER);
        }

        user.updateApplication();
        userRepository.save(user);
        return ApplicationDetailResponse.from(application);
    }
}
