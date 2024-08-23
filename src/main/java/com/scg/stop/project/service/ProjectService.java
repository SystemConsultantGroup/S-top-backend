package com.scg.stop.project.service;


import com.scg.stop.event.domain.EventPeriod;
import com.scg.stop.event.repository.EventPeriodRepository;
import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.file.repository.FileRepository;
import com.scg.stop.project.domain.*;
import com.scg.stop.project.dto.request.CommentRequest;
import com.scg.stop.project.dto.request.ProjectRequest;
import com.scg.stop.project.dto.response.CommentResponse;
import com.scg.stop.project.dto.response.ProjectDetailResponse;
import com.scg.stop.project.dto.response.ProjectResponse;
import com.scg.stop.project.repository.CommentRepository;
import com.scg.stop.project.repository.FavoriteProjectRepository;
import com.scg.stop.project.repository.LikeRepository;
import com.scg.stop.project.repository.ProjectRepository;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public Page<ProjectResponse> getProjects(String title, Integer year, ProjectCategory category, Pageable pageable, User user){

        Page<Project> projects = projectRepository.findProjects(title, year, category, pageable);

        Page<ProjectResponse> projectResponses = projects.map(project -> ProjectResponse.of(user, project));

        return projectResponses;
    }
  
    public ProjectDetailResponse createProject(ProjectRequest projectRequest) {
        File thumbnail = fileRepository.findById(projectRequest.getThumbnailId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT_THUMBNAIL));
        File poster = fileRepository.findById(projectRequest.getPosterId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT_POSTER));

        Project project = projectRequest.toEntity(null, thumbnail, poster);
        projectRepository.save(project);

        return getProject(project.getId(), null);
    }

    @Transactional(readOnly = true)
    public ProjectDetailResponse getProject(Long projectId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));

        return ProjectDetailResponse.of(user, project);
    }

    public ProjectDetailResponse updateProject(Long projectId, ProjectRequest projectRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));

        File thumbnail = fileRepository.findById(projectRequest.getThumbnailId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT_THUMBNAIL));
        File poster = fileRepository.findById(projectRequest.getPosterId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT_POSTER));

        Project newProject = projectRequest.toEntity(projectId, thumbnail, poster);
        project.update(newProject);

        return getProject(projectId, null);
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
            user.addFavoriteProject(newFavoriteProject);
        } catch (DataIntegrityViolationException e){
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
        user.removeFavoriteProject(favoriteProject);
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
            user.addLikes(newLike);
        } catch (DataIntegrityViolationException e){
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
        user.removeLikes(like);
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
}
