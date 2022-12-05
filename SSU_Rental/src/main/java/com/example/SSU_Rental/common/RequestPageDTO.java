package com.example.SSU_Rental.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestPageDTO {


    @Schema(description = "목록 페이지",type = "Integer",example = "1")
    private int page;

    @Schema(description = "목록 사이즈",type = "Integer",example = "5")
    private int size;

    @Schema(description = "SCHOOL or STUDENT",type = "String",example = "SCHOOL")
    private Group group;
}
