package com.example.SSU_Rental.board;


import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequest {

    @NotBlank(message = "제목은 공백이 될 수 없습니다.")
    @Schema(description = "게시글 제목")
    private String title;

    @NotBlank(message = "내용은 공백이 될 수 없습니다.")
    @Schema(description = "게시글 내용")
    private String content;



}
