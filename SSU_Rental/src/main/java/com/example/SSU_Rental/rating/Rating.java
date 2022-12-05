package com.example.SSU_Rental.rating;

import com.example.SSU_Rental.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Rating {

    @Id
    @GeneratedValue
    @Column(name = "rating_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  //다대일 관계 -> 평가 여러개에 member 1
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String content;

    @Column
    private int score;

    @Builder
    public Rating(Long id, Member member, String content, int score) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.score = score;
    }


    public static Rating makeRatingOne(Member member, RatingRequest ratingRequest) {
        return Rating.builder()
            .member(member)
            .content(ratingRequest.getContent())
            .score(ratingRequest.getScore())
            .build();
    }
}
