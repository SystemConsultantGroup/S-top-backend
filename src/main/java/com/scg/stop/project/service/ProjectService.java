package com.scg.stop.project.service;


import com.scg.stop.event.domain.EventPeriod;
import com.scg.stop.event.repository.EventPeriodRepository;
import com.scg.stop.file.domain.File;
import com.scg.stop.file.repository.FileRepository;
import com.scg.stop.project.domain.*;
import com.scg.stop.project.dto.request.CommentRequest;
import com.scg.stop.project.dto.request.ProjectRequest;
import com.scg.stop.project.dto.response.CommentResponse;
import com.scg.stop.project.dto.response.ProjectDetailResponse;
import com.scg.stop.project.dto.response.ProjectResponse;
import com.scg.stop.project.repository.*;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.global.infrastructure.EmailService;
import com.scg.stop.project.domain.Inquiry;
import com.scg.stop.project.domain.Project;
import com.scg.stop.project.dto.request.InquiryRequest;
import com.scg.stop.project.dto.response.InquiryDetailResponse;
import com.scg.stop.project.repository.ProjectRepository;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final FileRepository fileRepository;
    private final FavoriteProjectRepository favoriteProjectRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final EventPeriodRepository eventPeriodRepository;
    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;
    private final EmailService emailService;

    @Value("${spring.mail.adminEmail}")
    private String adminEmail;

    @Autowired
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Page<ProjectResponse> getProjects(String title, List<Integer> year, List<ProjectCategory> category, List<ProjectType> type, Pageable pageable, User user){
        boolean isEvent = year != null && year.size() == 1 && year.get(0) == LocalDateTime.now().getYear();
        Integer seed = (int) Math.floor(LocalDateTime.now().getHour() / 1.0);
        Page<Project> projects = isEvent
                ? projectRepository.findEventProjects(title, year, category, type, seed, pageable)
                : projectRepository.findProjects(title, year, category, type, pageable);

        Page<ProjectResponse> projectResponses = projects.map(project -> ProjectResponse.of(user, project));
        return projectResponses;
    }

    public ProjectDetailResponse createProject(ProjectRequest projectRequest, User user) {
        File thumbnail = fileRepository.findById(projectRequest.getThumbnailId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT_THUMBNAIL));
        File poster = fileRepository.findById(projectRequest.getPosterId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT_POSTER));

        Project project = projectRequest.toEntity(null, thumbnail, poster);
        projectRepository.save(project);

        return ProjectDetailResponse.of(user, project);
    }

    @Transactional(readOnly = true)
    public ProjectDetailResponse getProject(Long projectId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));
        return ProjectDetailResponse.of(user, project);
    }

    public ProjectDetailResponse updateProject(Long projectId, ProjectRequest projectRequest, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));

        File thumbnail = fileRepository.findById(projectRequest.getThumbnailId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT_THUMBNAIL));
        File poster = fileRepository.findById(projectRequest.getPosterId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT_POSTER));

        Project newProject = projectRequest.toEntity(projectId, thumbnail, poster);

        memberRepository.deleteAll(project.getMembers());
        project.update(newProject);

        return ProjectDetailResponse.of(user, project);
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));

        projectRepository.delete(project);
    }

    public void createProjectFavorite(Long projectId, User user){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));

        try {
            FavoriteProject newFavoriteProject = new FavoriteProject(null, project, user);
            favoriteProjectRepository.save(newFavoriteProject);
            project.addFavoriteProject(newFavoriteProject);
            userRepository.save(user);
        } catch (DataIntegrityViolationException e){ // DB 사이드에서 동시성 처리
            throw new BadRequestException(ExceptionCode.ALREADY_FAVORITE_PROJECT);
        }
    }

    public void deleteProjectFavorite(Long projectId, User user){
        FavoriteProject favoriteProject = favoriteProjectRepository.findByProjectIdAndUserId(projectId, user.getId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_FAVORITE_PROJECT));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));

        favoriteProjectRepository.delete(favoriteProject);
        project.removeFavoriteProject(favoriteProject);
        //userRepository.save(user);
    }

    public void createProjectLike(Long projectId, User user){
        EventPeriod eventPeriod = eventPeriodRepository.findByYear(LocalDateTime.now().getYear())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_EVENT_PERIOD));
        if (eventPeriod.getStart().isAfter(LocalDateTime.now()) || eventPeriod.getEnd().isBefore(LocalDateTime.now())){
            throw new BadRequestException(ExceptionCode.NOT_EVENT_PERIOD);
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));

        try {
            Likes newLike = new Likes(null, project, user);
            likeRepository.save(newLike);
            project.addLikes(newLike);
            userRepository.save(user);
        } catch (DataIntegrityViolationException e){ // DB 사이드에서 동시성 처리
            throw new BadRequestException(ExceptionCode.ALREADY_LIKE_PROJECT);
        }
    }

    public void deleteProjectLike(Long projectId, User user){
        EventPeriod eventPeriod = eventPeriodRepository.findByYear(LocalDateTime.now().getYear())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_EVENT_PERIOD));
        if (eventPeriod.getStart().isAfter(LocalDateTime.now()) || eventPeriod.getEnd().isBefore(LocalDateTime.now())){
            throw new BadRequestException(ExceptionCode.NOT_EVENT_PERIOD);
        }

        Likes like = likeRepository.findByProjectIdAndUserId(projectId, user.getId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_LIKE_PROJECT));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));

        likeRepository.delete(like);
        project.removeLikes(like);
        //userRepository.save(user);
    }

    public CommentResponse createProjectComment(Long projectId, User user, CommentRequest commentRequest){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));

        Comment comment = commentRequest.toEntity(project, user);
        commentRepository.save(comment);

        return CommentResponse.of(comment);
    }

    public void deleteProjectComment(Long projectId, Long commentId, User user){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_COMMENT));
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new BadRequestException(ExceptionCode.NOT_MATCH_USER);
        }

        commentRepository.delete(comment);
    }

    public Page<ProjectResponse> getAwardProjects(Integer year, Pageable page, User user){
        Page<Project> projects = projectRepository.findAwardProjects(year, page);
        Page<ProjectResponse> projectResponses = projects.map(project -> ProjectResponse.of(user, project));
        return projectResponses;
    }

    public InquiryDetailResponse createProjectInquiry(Long projectId, User user, InquiryRequest inquiryRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));
        Inquiry inquiry = Inquiry.createInquiry(
                inquiryRequest.getTitle(),
                inquiryRequest.getContent(),
                project,
                user
        );
        inquiryRepository.save(inquiry);
        emailService.sendEmail(adminEmail, inquiry.getTitle(), inquiry.getContent());
        return InquiryDetailResponse.of(
                inquiry.getId(),
                user.getName(),
                project.getId(),
                project.getName(),
                inquiry.getTitle(),
                inquiry.getContent(),
                false, // 막 생성된 문의는 답변이 달리지 않은 상태
                inquiry.getCreatedAt(),
                inquiry.getUpdatedAt()
        );

    }
}
