package com.example.SSU_Rental.member;

import com.example.SSU_Rental.image.ImageDTO;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEdit {

    private String name;

    private ImageDTO imageDTO;


    @Builder
    public MemberEdit(String name, ImageDTO imageDTO) {
        this.name = name;
        this.imageDTO = imageDTO;
    }
}
