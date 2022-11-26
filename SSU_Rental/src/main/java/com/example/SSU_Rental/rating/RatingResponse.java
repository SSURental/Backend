package com.example.SSU_Rental.rating;


import lombok.Getter;

@Getter
public class RatingResponse {

    private int score;

    private String content;


    public RatingResponse(int score, String content) {
        this.score = score;
        this.content = content;
    }

    public static RatingResponse from(Rating entity) {
        return new RatingResponse(entity.getRt_score(), entity.getRt_content());

    }
}
