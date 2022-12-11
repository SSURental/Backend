package com.example.SSU_Rental.member;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.image.ImageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequest {

    @NotEmpty
    @Schema(description = "로그인 아이디")
    private String loginId;

    @NotEmpty
    @Schema(description = "로그인 비밀번호")
    private String password;

    @NotEmpty
    @Schema(description = "회원 이름")
    private String name;

    @NotEmpty
    @Schema(description = "회원이 속한 그룹-> ex) 학생인지 혹은 학교측인지",example = "SCHOOL")
    private Group memberGroup;

    @Schema(description = "회원 사진 URL")
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
