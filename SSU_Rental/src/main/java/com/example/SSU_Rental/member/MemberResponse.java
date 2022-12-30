package com.example.SSU_Rental.member;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.image.ImageDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {


    private Long id;

    private String loginId;

    private String name;

    private Group memberGroup;

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
