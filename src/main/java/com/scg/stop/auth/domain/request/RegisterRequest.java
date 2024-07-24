package com.scg.stop.auth.domain.request;

import com.scg.stop.user.domain.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterRequest {

    private String name;
    private String phoneNumber;
    private UserType userType;

}
