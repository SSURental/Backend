package com.example.SSU_Rental.rating;

import com.example.SSU_Rental.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Table(name = "rating")
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
    private String rt_content;

    @Column
    private int rt_score;

    @Builder
    public Rating(Long id, Member member, String rt_content, int rt_score) {
        this.id = id;
        this.member = member;
        this.rt_content = rt_content;
        this.rt_score = rt_score;
    }


    public static Rating makeRatingOne(Member member, RatingRequest ratingRequest) {
        return Rating.builder()
            .member(member)
            .rt_content(ratingRequest.getContent())
            .rt_score(ratingRequest.getScore())
            .build();
    }
}
