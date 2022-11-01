package com.example.SSU_Rental.boardrp;


import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.member.Member;
import lombok.Builder;
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
    @GeneratedValue
    @Column(name = "boardrp_id")
    private Long id;

    @Column
    @ManyToOne(fetch = FetchType.LAZY)  // -> 댓글 여러개에 board 1개
    @JoinColumn(name = "board_id")
    private Board board;


    @Column
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String content;


    @Builder
    public Boardrp(Long id, Board board, Member member, String content) {
        this.id = id;
        this.board = board;
        this.member = member;
        this.content = content;
    }

    public void modify(String content){
        this.content = content;
    }
}
