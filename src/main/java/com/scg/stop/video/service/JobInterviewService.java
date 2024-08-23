package com.scg.stop.video.service;

import com.scg.stop.user.domain.User;
import com.scg.stop.video.domain.FavoriteVideo;
import com.scg.stop.video.domain.JobInterviewCategory;
import com.scg.stop.video.domain.JobInterview;
import com.scg.stop.video.dto.request.JobInterviewRequest;
import com.scg.stop.video.dto.response.JobInterviewResponse;
import com.scg.stop.video.repository.FavoriteVideoRepository;
import com.scg.stop.video.repository.JobInterviewRepository;
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
    private final FavoriteVideoRepository favoriteVideoRepository;

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
        JobInterview newJobInterview = jobInterviewRepository.save(
                JobInterview.from(
                        req.getTitle(),
                        req.getYoutubeId(),
                        req.getYear(),
                        req.getTalkerBelonging(),
                        req.getTalkerName(),
                        req.getCategory()
                )
        );
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
        jobInterview.updateJobInterview(
                req.getTitle(),
                req.getYoutubeId(),
                req.getYear(),
                req.getTalkerBelonging(),
                req.getTalkerName(),
                req.getCategory()
        );
        return JobInterviewResponse.from(jobInterview);
    }


}
