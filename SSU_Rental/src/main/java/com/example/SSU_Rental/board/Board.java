package com.example.SSU_Rental.board;

import member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name="board")
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long board_id;

    @Column
    @ManyToOne  //다대일 관계 -> 게시글 여러개에 member 1
    private Member member;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Long see_cnt;

    @Column
    private Long rec_cnt;

    @Column
    private Long warn_cnt;

}
