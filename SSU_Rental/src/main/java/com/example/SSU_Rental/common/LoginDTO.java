package com.example.SSU_Rental.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDTO {

    private String loginId;

    private String password;

    public LoginDTO(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
