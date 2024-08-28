package com.scg.stop.user.service;

import com.scg.stop.domain.project.domain.Inquiry;
import com.scg.stop.domain.project.domain.Project;
import com.scg.stop.domain.project.dto.response.ProjectResponse;
import com.scg.stop.domain.project.repository.FavoriteProjectRepository;
import com.scg.stop.domain.project.repository.InquiryRepository;
import com.scg.stop.domain.proposal.domain.Proposal;
import com.scg.stop.domain.proposal.repository.ProposalRepository;
import com.scg.stop.domain.video.domain.JobInterview;
import com.scg.stop.domain.video.domain.Talk;
import com.scg.stop.domain.video.dto.response.VideoResponse;
import com.scg.stop.domain.video.repository.FavoriteVideoRepository;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.user.domain.*;
import com.scg.stop.user.dto.request.UserUpdateRequest;
import com.scg.stop.user.dto.response.UserInquiryResponse;
import com.scg.stop.user.dto.response.UserProposalResponse;
import com.scg.stop.user.dto.response.UserResponse;
import com.scg.stop.user.repository.DepartmentRepository;
import com.scg.stop.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.micrometer.common.util.StringUtils.isBlank;
import static io.micrometer.common.util.StringUtils.isNotBlank;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final InquiryRepository inquiryRepository;
    private final ProposalRepository proposalRepository;
    private final FavoriteProjectRepository favoriteProjectRepository;
    private final FavoriteVideoRepository favoriteVideoRepository;

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
        } else if (Arrays.asList(UserType.INACTIVE_PROFESSOR, UserType.COMPANY, UserType.INACTIVE_COMPANY, UserType.PROFESSOR).contains(user.getUserType())) {
            return UserResponse.of(
                    user,
                    user.getApplication().getDivision(),
                    user.getApplication().getPosition(),
                    null,
                    null
            );
        } else {
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
            if (!isNull(request.getDivision()) && isBlank(request.getDivision()) ||
                !isNull(request.getPosition()) && isBlank(request.getPosition())) {

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

    public List<UserInquiryResponse> getUserInquiries(User user) {
        List<Inquiry> inquiries = inquiryRepository.findByUser(user);
        return inquiries.stream()
                .map(UserInquiryResponse::from)
                .collect(Collectors.toList());
    }

    public List<UserProposalResponse> getUserProposals(User user) {
        List<Proposal> proposals = proposalRepository.findByUser(user);
        return proposals.stream()
                .map(UserProposalResponse::from)
                .collect(Collectors.toList());
    }

    public List<?> getUserFavorites(User user, FavoriteType type) {
        if (type.equals(FavoriteType.PROJECT)) {
            List<Project> projects = favoriteProjectRepository.findAllByUser(user);
            return projects.stream()
                    .map(project -> ProjectResponse.of(project.getId(), project.getName()))
                    .collect(Collectors.toList());
        }
        else if (type.equals(FavoriteType.TALK)) {
            List<Talk> talks = favoriteVideoRepository.findTalksByUser(user);
            return talks.stream()
                    .map(talk -> VideoResponse.of(talk.getId(), talk.getTitle(), talk.getYoutubeId()))
                    .collect(Collectors.toList());
        }
        else { // if (type.equals(FavoriteType.JOBINTERVIEW)) {
            List<JobInterview> jobInterviews = favoriteVideoRepository.findJobInterviewsByUser(user);
            return jobInterviews.stream()
                    .map(jobInterview -> VideoResponse.of(jobInterview.getId(), jobInterview.getTitle(), jobInterview.getYoutubeId()))
                    .collect(Collectors.toList());
        }

    }

}