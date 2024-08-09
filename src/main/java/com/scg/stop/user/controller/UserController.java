package com.scg.stop.user.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.dto.request.UserUpdateRequest;
import com.scg.stop.user.dto.response.UserResponse;
import com.scg.stop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMe(
            @AuthUser(accessType = {AccessType.ALL}) User user,
            @RequestBody UserUpdateRequest request
    ) {
        UserResponse updatedUserResponse = userService.updateMe(user, request);
        return ResponseEntity.ok(updatedUserResponse);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(@AuthUser(accessType = {AccessType.ALL}) User user) {
        userService.deleteMe(user);
        return ResponseEntity.noContent().build();
    }
}
