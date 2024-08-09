package com.scg.stop.domain.project.service;


import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.file.repository.FileRepository;
import com.scg.stop.domain.project.domain.*;
import com.scg.stop.domain.project.dto.request.ProjectRequest;
import com.scg.stop.domain.project.dto.response.ProjectDetailResponse;
import com.scg.stop.domain.project.dto.response.ProjectResponse;
import com.scg.stop.domain.project.repository.FavoriteProjectRepository;
import com.scg.stop.domain.project.repository.LikeRepository;
import com.scg.stop.domain.project.repository.ProjectRepository;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<ProjectResponse> getProjects(String title, Integer year, ProjectCategory category, Pageable pageable){

        Page<Project> projects = projectRepository.findProjects(title, year, category, pageable);

        Page<ProjectResponse> projectResponses = projects.map(project -> {
            List<String> studentNames = project.getMembers().stream()
                    .filter(member -> member.getRole() == Role.STUDENT)
                    .map(Member::getName)
                    .collect(Collectors.toList());
            List<String> professorNames = project.getMembers().stream()
                    .filter(member -> member.getRole() == Role.PROFESSOR)
                    .map(Member::getName)
                    .collect(Collectors.toList());
            return ProjectResponse.of(studentNames, professorNames, project);
        });

        return projectResponses;
    }
  
    public ProjectDetailResponse createProject(ProjectRequest projectRequest) {
        File thumbnail = fileRepository.findById(projectRequest.getThumbnailId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT_THUMBNAIL));
        File poster = fileRepository.findById(projectRequest.getPosterId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT_POSTER));

        Project project = projectRequest.toEntity(null, thumbnail, poster);

        projectRepository.save(project);

        return getProject(project.getId());
    }

    @Transactional(readOnly = true)
    public ProjectDetailResponse getProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));

        List<String> studentNames = project.getMembers().stream()
                .filter(member -> member.getRole() == Role.STUDENT)
                .map(Member::getName)
                .collect(Collectors.toList());
        List<String> professorNames = project.getMembers().stream()
                .filter(member -> member.getRole() == Role.PROFESSOR)
                .map(Member::getName)
                .collect(Collectors.toList());

        return ProjectDetailResponse.of(studentNames, professorNames, project);
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

        return getProject(projectId);
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));

        projectRepository.delete(project);
    }


    public void createProjectFavorite(Long projectId, Long userId){
        boolean exists = favoriteProjectRepository.findByProjectIdAndUserId(projectId, userId).isPresent();
        if (exists) { // Todo: error를 던지진 말고 그냥 요청을 취소하는 방식은 어떨까..
            throw new BadRequestException(ExceptionCode.ALREADY_FAVORITE_PROJECT);
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_USER_ID));

        FavoriteProject newFavoriteProject = new FavoriteProject(null, project, user);
        favoriteProjectRepository.save(newFavoriteProject);
    }

    public void deleteProjectFavorite(Long projectId, Long userId){
        FavoriteProject favoriteProject = favoriteProjectRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_FAVORITE_PROJECT));

        favoriteProjectRepository.delete(favoriteProject);
    }

    public void createProjectLike(Long projectId, Long userId){
        boolean exists = likeRepository.findByProjectIdAndUserId(projectId, userId).isPresent();
        if (exists) { // Todo: error를 던지진 말고 그냥 요청을 취소하는 방식은 어떨까..
            throw new BadRequestException(ExceptionCode.ALREADY_LIKE_PROJECT);
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_USER_ID));

        Likes newLike = new Likes(null, project, user);
        likeRepository.save(newLike);
    }

    public void deleteProjectLike(Long projectId, Long userId){
        Likes like = likeRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_FAVORITE_PROJECT));

        likeRepository.delete(like);
    }
}
