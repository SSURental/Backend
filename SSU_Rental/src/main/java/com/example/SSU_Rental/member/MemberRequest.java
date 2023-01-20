package com.example.SSU_Rental.member;

import com.example.SSU_Rental.common.Enum;
import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.image.ImageDTO;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequest {

    @NotEmpty(message = "로그인 아이디는 공백이 될 수 없습니다.")
    private String loginId;

    @NotEmpty(message = "비밀번호는 공백이 될 수 없습니다.")
    private String password;

    @NotEmpty(message = "회원 이름은 공백이 될 수 없습니다.")
    private String name;

    @Enum(enumClass = Group.class, ignoreCase = true,message = "Group에는 STUDENT, SCHOOL만 들어갈 수 있습니다.")
    private String group;

    private ImageDTO imageDTO;

    @Builder
    public MemberRequest(String loginId, String password, String name, String group,ImageDTO imageDTO) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.group = group;
        this.imageDTO = imageDTO;
    }
}
