package com.example.SSU_Rental.rating;

import lombok.Getter;

@Getter
public class RatingRequest {


    private int score;

    private String content;

    public RatingRequest(int score, String content) {
        this.score = score;
        this.content = content;
    }
}
