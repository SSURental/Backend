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

    private String group;

    private ImageDTO imageDTO;


    @Builder
    public MemberResponse(Long id, String loginId, String name,
        String group, ImageDTO imageDTO) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.group = group;
        this.imageDTO = imageDTO;
    }

    public static MemberResponse from(Member member){
        ImageDTO imageDTO = new ImageDTO(member.getMemberImage().getImgName());

        return MemberResponse.builder()
            .id(member.getId())
            .loginId(member.getLoginId())
            .name(member.getName())
            .group(member.getMemberGroup().getValue())
            .imageDTO(imageDTO)
            .build();
    }

}
