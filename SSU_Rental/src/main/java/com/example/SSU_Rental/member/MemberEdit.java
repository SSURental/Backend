package com.example.SSU_Rental.member;

import com.example.SSU_Rental.image.ImageDTO;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEdit {

    @NotBlank(message = "사용자 이름을 입력하시오.")
    private String name;

    private ImageDTO imageDTO;

}
