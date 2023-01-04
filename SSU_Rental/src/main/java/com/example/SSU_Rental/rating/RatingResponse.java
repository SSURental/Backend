package com.example.SSU_Rental.rating;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatingResponse {


    private Long id;

    private int score;

    private String nickname;

    private String content;

    private Long itemId;



    public static RatingResponse from(Rating entity) {
        return new RatingResponse(entity.getId(), entity.getScore(),entity.getMember().getName(),entity.getContent(),entity.getItem().getId());

    }
}
