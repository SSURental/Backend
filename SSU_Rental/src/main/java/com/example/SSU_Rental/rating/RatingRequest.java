package com.example.SSU_Rental.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
public class RatingRequest {


    @Range(min = 0,max = 10)
    @Schema(description = "리뷰 점수(0~10)")
    private int score;

    @NotBlank
    @Schema(description = "리뷰 내용")
    private String content;

    public RatingRequest(int score, String content) {
        this.score = score;
        this.content = content;
    }
}
