package com.example.SSU_Rental.boardrp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardrpRequest {

    private Long member_id;

    private String content;

}
