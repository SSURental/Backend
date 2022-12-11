package com.example.SSU_Rental.member;

import com.example.SSU_Rental.image.ImageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEdit {

    @Schema(description = "사용자 이름")
    private String name;

    @Schema(description = "회원 사진 URL")
    private ImageDTO imageDTO;

}
