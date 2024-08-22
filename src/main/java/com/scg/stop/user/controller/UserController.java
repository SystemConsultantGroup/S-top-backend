package com.scg.stop.user.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.dto.request.UserUpdateRequest;
import com.scg.stop.user.dto.response.UserInquiryResponse;
import com.scg.stop.user.dto.response.UserProposalResponse;
import com.scg.stop.user.dto.response.UserResponse;
import com.scg.stop.user.service.UserService;
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
}
