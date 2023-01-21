package com.example.SSU_Rental.rating;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RatingRequest {


    @Range(min = 1,max = 10,message = "리뷰 점수는 0~10점 사이 입니다.")
    private Integer score;

    @NotBlank(message = "리뷰는 공백이 될 수 없습니다.")
    private String content;

    @Builder
    public RatingRequest(int score, String content) {
        this.score = score;
        this.content = content;
    }
}
