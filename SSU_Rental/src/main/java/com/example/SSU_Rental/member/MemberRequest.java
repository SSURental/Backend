package com.example.SSU_Rental.member;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.image.ImageDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberRequest {

    private String login_id;
    private String pw;
    private String name;
    private Group group;
    private ImageDTO imageDTO;

    @Builder
    public MemberRequest(String login_id, String pw, String name, Group group,ImageDTO imageDTO) {
        this.login_id = login_id;
        this.pw = pw;
        this.name = name;
        this.group = group;
        this.imageDTO = imageDTO;
    }
}
