package com.scg.stop.domain.video.service;

import com.scg.stop.domain.video.domain.JobInterviewCategory;
import com.scg.stop.domain.video.domain.JobInterview;
import com.scg.stop.domain.video.dto.request.JobInterviewRequest;
import com.scg.stop.domain.video.dto.response.JobInterviewResponse;
import com.scg.stop.domain.video.repository.JobInterviewRepository;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JobInterviewService {
    private final JobInterviewRepository jobInterviewRepository;

    @Transactional(readOnly = true)
    public JobInterviewResponse getJobInterview(Long id) {
        JobInterview jobInterview = jobInterviewRepository.findById(id).orElseThrow(() ->
                new BadRequestException(ExceptionCode.ID_NOT_FOUND));
        return JobInterviewResponse.from(jobInterview);
    }

    @Transactional(readOnly = true)
    public Page<JobInterviewResponse> getJobInterviews(Integer year, JobInterviewCategory category, String title, Pageable pageable) {
        return jobInterviewRepository.findJobInterviews(year, category, title, pageable);
    }

    public JobInterviewResponse createJobInterview(JobInterviewRequest req) {
        JobInterview newJobInterview = jobInterviewRepository.save(JobInterview.from(req));
        return JobInterviewResponse.from(newJobInterview);
    }

    public void deleteJobInterviewById(Long id) {
        JobInterview jobInterview = jobInterviewRepository.findById(id).orElseThrow(() ->
                new BadRequestException(ExceptionCode.ID_NOT_FOUND));
        jobInterviewRepository.delete(jobInterview);
    }

    public JobInterviewResponse updateJobInterview(Long id, JobInterviewRequest req) {
        JobInterview jobInterview = jobInterviewRepository.findById(id).orElseThrow(() ->
                new BadRequestException(ExceptionCode.ID_NOT_FOUND));
        jobInterview.updateJobInterview(req);
        return JobInterviewResponse.from(jobInterview);
    }
}
