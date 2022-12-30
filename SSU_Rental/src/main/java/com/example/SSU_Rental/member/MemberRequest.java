package com.example.SSU_Rental.member;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.image.ImageDTO;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequest {

    @NotEmpty(message = "로그인 아이디는 공백이 될 수 없습니다.")
    private String loginId;

    @NotEmpty(message = "비밀번호는 공백이 될 수 없습니다.")
    private String password;

    @NotEmpty(message = "회원 이름은 공백이 될 수 없습니다.")
    private String name;

    private Group memberGroup;

    private ImageDTO imageDTO;

    @Builder
    public MemberRequest(String loginId, String password, String name, Group memberGroup,ImageDTO imageDTO) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.memberGroup = memberGroup;
        this.imageDTO = imageDTO;
    }
}
