package com.example.SSU_Rental.rating;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RatingEdit {

    private String content;

    private Integer score;

    @Builder
    public RatingEdit(String content, Integer score) {
        this.content = content;
        this.score = score;
    }
}
