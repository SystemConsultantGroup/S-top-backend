package com.scg.stop.user.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.dto.response.UserResponse;
import com.scg.stop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthUser(accessType = {AccessType.ALL}) User user) {
        UserResponse userResponse = userService.getUserProfile(user);
        return ResponseEntity.ok(userResponse);
    }
}
