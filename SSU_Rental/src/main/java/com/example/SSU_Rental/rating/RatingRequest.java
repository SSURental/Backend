package com.example.SSU_Rental.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RatingRequest {


    @Schema(description = "리뷰 점수(0~10)")
    private int score;

    @Schema(description = "리뷰 내용")
    private String content;

    public RatingRequest(int score, String content) {
        this.score = score;
        this.content = content;
    }
}
