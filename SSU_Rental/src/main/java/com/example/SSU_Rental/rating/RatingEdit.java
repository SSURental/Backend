package com.example.SSU_Rental.rating;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RatingEdit {

    private String content;

    private Integer score;

    @Builder
    public RatingEdit(String content, Integer score) {
        this.content = content;
        this.score = score;
    }
}
