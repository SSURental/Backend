package com.example.SSU_Rental.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestPageDTO {


    private int page;

    private int size;

    private Group group;
}
