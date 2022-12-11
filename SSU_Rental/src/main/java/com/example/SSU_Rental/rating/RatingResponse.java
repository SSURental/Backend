package com.example.SSU_Rental.rating;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatingResponse {


    @Schema(description = "리뷰 아이디")
    private Long id;

    @Schema(description = "리뷰 점수")
    private int score;

    @Schema(description = "리뷰 작성자")
    private String nickname;

    @Schema(description = "리뷰 내용")
    private String content;

    @Schema(description = "리뷰 받은 아이템의 아이디")
    private Long itemId;



    public static RatingResponse from(Rating entity) {
        return new RatingResponse(entity.getId(), entity.getScore(),entity.getMember().getName(),entity.getContent(),entity.getItem().getId());

    }
}
