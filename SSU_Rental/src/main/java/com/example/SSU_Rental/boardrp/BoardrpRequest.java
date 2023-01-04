package com.example.SSU_Rental.boardrp;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardrpRequest {

    @NotBlank(message = "내용은 공백이 될 수 없습니다.")
    private String content;

}
