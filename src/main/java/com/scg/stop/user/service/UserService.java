package com.scg.stop.user.service;

import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.project.domain.Inquiry;
import com.scg.stop.project.domain.Project;
import com.scg.stop.project.dto.response.ProjectResponse;
import com.scg.stop.project.repository.FavoriteProjectRepository;
import com.scg.stop.project.repository.InquiryRepository;
import com.scg.stop.proposal.domain.Proposal;
import com.scg.stop.proposal.repository.ProposalRepository;
import com.scg.stop.user.domain.Department;
import com.scg.stop.user.domain.Student;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import com.scg.stop.user.dto.request.UserUpdateRequest;
import com.scg.stop.user.dto.response.UserInquiryResponse;
import com.scg.stop.user.dto.response.UserProposalResponse;
import com.scg.stop.user.dto.response.UserResponse;
import com.scg.stop.user.repository.DepartmentRepository;
import com.scg.stop.user.repository.UserRepository;
import com.scg.stop.video.domain.JobInterview;
import com.scg.stop.video.domain.Talk;
import com.scg.stop.video.dto.response.JobInterviewUserResponse;
import com.scg.stop.video.dto.response.TalkUserResponse;
import com.scg.stop.video.repository.FavoriteVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.micrometer.common.util.StringUtils.isBlank;
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

    @Transactional(readOnly = true)
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
        } else if (Arrays.asList(UserType.INACTIVE_PROFESSOR, UserType.COMPANY, UserType.INACTIVE_COMPANY, UserType.PROFESSOR).contains(user.getUserType())) {
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

    @Transactional(readOnly = true)
    public List<UserInquiryResponse> getUserInquiries(User user) {
        List<Inquiry> inquiries = inquiryRepository.findByUser(user);
        return inquiries.stream()
                .map(UserInquiryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserProposalResponse> getUserProposals(User user) {
        List<Proposal> proposals = proposalRepository.findByUser(user);
        return proposals.stream()
                .map(UserProposalResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getUserFavoriteProjects(User user) {
        List<Project> projects = favoriteProjectRepository.findAllByUser(user);
        return projects.stream()
                .map(project -> ProjectResponse.of(user, project))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TalkUserResponse> getUserFavoriteTalks(User user) {
        List<Talk> talks = favoriteVideoRepository.findTalksByUser(user);
        return talks.stream()
                .map(talk -> TalkUserResponse.from(talk, true))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<JobInterviewUserResponse> getUserFavoriteInterviews(User user) {
        List<JobInterview> jobInterviews = favoriteVideoRepository.findJobInterviewsByUser(user);
        return jobInterviews.stream()
                .map(jobInterview -> JobInterviewUserResponse.from(jobInterview, true))
                .collect(Collectors.toList());
    }

}
