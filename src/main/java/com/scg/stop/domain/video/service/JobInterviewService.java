package com.scg.stop.domain.video.service;

import com.scg.stop.domain.video.domain.Category;
import com.scg.stop.domain.video.domain.JobInterview;
import com.scg.stop.domain.video.dto.JobInterviewDTO;
import com.scg.stop.domain.video.repository.JobInterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobInterviewService {
    private final JobInterviewRepository jobInterviewRepository;

    @Transactional(readOnly = true)
    public JobInterviewDTO.Response getJobInterview(Long id) {
        JobInterview jobInterview = jobInterviewRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("요청한 ID에 해당하는 잡페어 인터뷰가 없습니다."));
        return new JobInterviewDTO.Response(jobInterview);
    }

    @Transactional(readOnly = true)
    public Page<JobInterviewDTO.Response> getJobInterviews(Integer year, Category category, String title,Pageable pageable) {
        return jobInterviewRepository.findJobInterviews(year, category, title, pageable);
    }

    @Transactional
    public JobInterviewDTO.Response createJobInterview(JobInterviewDTO.Request req) {
        JobInterview newJobInterview = jobInterviewRepository.save(req.toEntity());
        return new JobInterviewDTO.Response(newJobInterview);
    }

    @Transactional
    public void deleteJobInterviewById(Long id) {
        JobInterview jobInterview = jobInterviewRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("요청한 ID에 해당하는 잡페어 인터뷰가 없습니다."));
        jobInterviewRepository.delete(jobInterview);
    }

    @Transactional
    public JobInterviewDTO.Response updateJobInterview(Long id, JobInterviewDTO.Request req) {
        int result = jobInterviewRepository.updateJobInterview(id, req);
        if (result < 1) {
            throw new IllegalArgumentException("요청한 ID에 해당하는 잡페어 인터뷰가 없습니다.");
        }
        JobInterview jobInterview = jobInterviewRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("요청한 ID에 해당하는 잡페어 인터뷰가 없습니다."));
        return new JobInterviewDTO.Response(jobInterview);
    }




}
