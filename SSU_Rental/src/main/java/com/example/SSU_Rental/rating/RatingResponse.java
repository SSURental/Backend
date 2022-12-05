package com.example.SSU_Rental.rating;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RatingResponse {

    @Schema(description = "리뷰 점수")
    private int score;

    @Schema(description = "리뷰 내용")
    private String content;


    public RatingResponse(int score, String content) {
        this.score = score;
        this.content = content;
    }

    public static RatingResponse from(Rating entity) {
        return new RatingResponse(entity.getScore(), entity.getContent());

    }
}
