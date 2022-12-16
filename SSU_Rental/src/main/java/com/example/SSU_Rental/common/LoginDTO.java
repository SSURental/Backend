package com.example.SSU_Rental.common;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDTO {

    @NotBlank(message = "아이디는 공백이 될 수 없습니다. ")
    private String loginId;

    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.")
    private String password;

    public LoginDTO(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
