package com.scg.stop.user.service;

import static com.scg.stop.global.exception.ExceptionCode.ALREADY_VERIFIED_USER;
import static com.scg.stop.global.exception.ExceptionCode.NOT_FOUND_APPLICATION_ID;

import com.scg.stop.user.domain.Application;
import com.scg.stop.user.domain.ApplicationStatus;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.dto.response.ApplicationDetailResponse;
import com.scg.stop.user.dto.response.ApplicationListResponse;
import com.scg.stop.user.repository.ApplicationRepository;
import com.scg.stop.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    public Page<ApplicationListResponse> getApplications(Pageable pageable) {
        Page<Application> filteredApplications = applicationRepository.findByStatus(ApplicationStatus.INACTIVE, pageable);
        return filteredApplications.map(ApplicationListResponse::from);
    }

    @Transactional(readOnly = true)
    public ApplicationDetailResponse getApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_APPLICATION_ID));
        if(application.getStatus() == ApplicationStatus.ACTIVE) {
            throw new BadRequestException(ALREADY_VERIFIED_USER);
        }
        return ApplicationDetailResponse.from(application);
    }

    public ApplicationDetailResponse approveApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_APPLICATION_ID));
        if(application.getStatus() == ApplicationStatus.ACTIVE) {
            throw new BadRequestException(ALREADY_VERIFIED_USER);
        }

        User user = application.getUser();
        user.activateUser();
        return ApplicationDetailResponse.from(application);
    }

    public void rejectApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_APPLICATION_ID));
        application.reject();
    }
}
