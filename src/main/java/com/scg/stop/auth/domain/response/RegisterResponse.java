package com.scg.stop.auth.domain.response;

import com.scg.stop.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class RegisterResponse {
    private String name;
    private String email;
    private String phone;

    private RegisterResponse(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public static RegisterResponse from(User user) {
        return new RegisterResponse(
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
    }
}
