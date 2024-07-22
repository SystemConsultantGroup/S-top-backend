package com.scg.stop.domain.video.service;

import com.scg.stop.domain.video.domain.JobInterviewCategory;
import com.scg.stop.domain.video.domain.JobInterview;
import com.scg.stop.domain.video.dto.JobInterviewDto;
import com.scg.stop.domain.video.repository.JobInterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobInterviewService {
    private final JobInterviewRepository jobInterviewRepository;

    @Transactional(readOnly = true)
    public JobInterviewDto.Response getJobInterview(Long id) {
        JobInterview jobInterview = jobInterviewRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("요청한 ID에 해당하는 잡페어 인터뷰가 없습니다."));
        return new JobInterviewDto.Response(jobInterview);
    }

    @Transactional(readOnly = true)
    public Page<JobInterviewDto.Response> getJobInterviews(Integer year, JobInterviewCategory category, String title, Pageable pageable) {
        return jobInterviewRepository.findJobInterviews(year, category, title, pageable);
    }

    @Transactional
    public JobInterviewDto.Response createJobInterview(JobInterviewDto.Request req) {
        JobInterview newJobInterview = jobInterviewRepository.save(req.toEntity());
        return new JobInterviewDto.Response(newJobInterview);
    }

    @Transactional
    public void deleteJobInterviewById(Long id) {
        JobInterview jobInterview = jobInterviewRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("요청한 ID에 해당하는 잡페어 인터뷰가 없습니다."));
        jobInterviewRepository.delete(jobInterview);
    }

    @Transactional
    public JobInterviewDto.Response updateJobInterview(Long id, JobInterviewDto.Request req) {
        int result = jobInterviewRepository.updateJobInterview(id, req);
        if (result < 1) {
            throw new IllegalArgumentException("요청한 ID에 해당하는 잡페어 인터뷰가 없습니다.");
        }
        JobInterview jobInterview = jobInterviewRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("요청한 ID에 해당하는 잡페어 인터뷰가 없습니다."));
        return new JobInterviewDto.Response(jobInterview);
    }




}
