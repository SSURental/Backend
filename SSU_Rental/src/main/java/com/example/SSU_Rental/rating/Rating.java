package com.example.SSU_Rental.rating;

import com.example.SSU_Rental.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Table(name="rating")
@Entity
public class Rating {
    @Id
    @GeneratedValue
    @Column(name = "rating_id")
    private Long rating_id;

    @Column
    @ManyToOne(fetch = FetchType.LAZY)  //다대일 관계 -> 평가 여러개에 member 1
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String rt_content;

    @Column
    private String rt_score;
}
