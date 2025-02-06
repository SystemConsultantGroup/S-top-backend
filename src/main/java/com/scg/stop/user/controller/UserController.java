package com.scg.stop.user.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.project.dto.response.ProjectResponse;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.dto.request.UserUpdateRequest;
import com.scg.stop.user.dto.response.FavoriteResponse;
import com.scg.stop.user.dto.response.UserInquiryResponse;
import com.scg.stop.user.dto.response.UserProposalResponse;
import com.scg.stop.user.dto.response.UserResponse;
import com.scg.stop.user.service.UserService;
import com.scg.stop.video.dto.response.JobInterviewUserResponse;
import com.scg.stop.video.dto.response.TalkUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthUser(accessType = {AccessType.ALL}) User user) {
        UserResponse userResponse = userService.getMe(user);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(
            @AuthUser(accessType = {AccessType.ALL}) User user,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        UserResponse updatedUserResponse = userService.updateMe(user, request);
        return ResponseEntity.ok(updatedUserResponse);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(@AuthUser(accessType = {AccessType.ALL}) User user) {
        userService.deleteMe(user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/inquiries")
    public ResponseEntity<List<UserInquiryResponse>> getUserInquiries(@AuthUser(accessType = {AccessType.ALL}) User user) {
        List<UserInquiryResponse> inquiries = userService.getUserInquiries(user);
        return ResponseEntity.ok(inquiries);
    }

    @GetMapping("/proposals")
    public ResponseEntity<List<UserProposalResponse>> getUserProposals(@AuthUser(accessType = {AccessType.ALL}) User user) {
        List<UserProposalResponse> proposals = userService.getUserProposals(user);
        return ResponseEntity.ok(proposals);
    }

    @GetMapping("/favorites/projects")
    public ResponseEntity<List<ProjectResponse>> getUserFavoriteProjects(@AuthUser(accessType = {AccessType.ALL}) User user) {
        List<ProjectResponse> userFavoriteProjects = userService.getUserFavoriteProjects(user);
        return ResponseEntity.ok(userFavoriteProjects);
    }

    @GetMapping("/favorites/talks")
    public ResponseEntity<List<TalkUserResponse>> getUserFavoriteTalks(@AuthUser(accessType = {AccessType.ALL}) User user) {
        List<TalkUserResponse> userFavoriteTalks = userService.getUserFavoriteTalks(user);
        return ResponseEntity.ok(userFavoriteTalks);
    }

    @GetMapping("/favorites/jobInterviews")
    public ResponseEntity<List<JobInterviewUserResponse>> getUserFavoriteInterviews(@AuthUser(accessType = {AccessType.ALL}) User user) {
        List<JobInterviewUserResponse> userFavoriteInterviews = userService.getUserFavoriteInterviews(user);
        return ResponseEntity.ok(userFavoriteInterviews);
    }
}
