package com.example.SSU_Rental.board;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardRequest {


    @NotBlank
    private Long member_id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;


}
