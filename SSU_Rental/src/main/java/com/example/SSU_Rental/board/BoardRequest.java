package com.example.SSU_Rental.board;


import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequest {

    @NotBlank(message = "제목은 공백이 될 수 없습니다.")
    private String title;

    @NotBlank(message = "내용은 공백이 될 수 없습니다.")
    private String content;



}
