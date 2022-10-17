package com.example.SSU_Rental.boardrp;


import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name="boardrp")
@Entity
public class Boardrp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardrp_id;

    @Column
    @ManyToOne  // -> 댓글 여러개에 board 1개
    private Board board;


    @Column
    @ManyToMany  //다대일 관계 -> 댓글 여러개에 member 여러개
    private Member member;

    @Column
    private String rp_content;


}
