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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rating_id;

    @Column
    @ManyToOne  //다대일 관계 -> 평가 여러개에 member 1
    private Member member;

    @Column
    private String rt_content;

    @Column
    private String rt_score;
}
