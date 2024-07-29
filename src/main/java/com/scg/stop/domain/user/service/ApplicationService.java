package com.scg.stop.domain.user.service;

import com.scg.stop.domain.user.dto.response.ApplicationListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    public Page<ApplicationListResponse> getApplications(Pageable pageable) {
        return null;
    }
}
