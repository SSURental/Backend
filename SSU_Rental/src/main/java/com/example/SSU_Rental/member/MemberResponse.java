package com.example.SSU_Rental.member;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.image.ImageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {


    @Schema(description = "회원 아이디")
    private Long id;

    @Schema(description = "회원 로그인 아이디")
    private String loginId;

    @Schema(description = "회원 이름")
    private String name;

    @Schema(description = "회원이 속한 그룹-> ex 학생인지 학교측인지",example = "SCHOOL")
    private Group memberGroup;

    @Schema(description = "회원 사진 URL")
    private ImageDTO imageDTO;


    @Builder
    public MemberResponse(Long id, String loginId, String name,
        Group memberGroup, ImageDTO imageDTO) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.memberGroup = memberGroup;
        this.imageDTO = imageDTO;
    }

    public static MemberResponse from(Member member){
        ImageDTO imageDTO = new ImageDTO(member.getMemberImage().getImgName());

        return MemberResponse.builder()
            .id(member.getId())
            .loginId(member.getLoginId())
            .name(member.getName())
            .memberGroup(member.getMemberGroup())
            .imageDTO(imageDTO)
            .build();
    }

}
