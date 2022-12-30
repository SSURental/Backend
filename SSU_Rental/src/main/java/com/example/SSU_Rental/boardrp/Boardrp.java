package com.example.SSU_Rental.boardrp;


import static com.example.SSU_Rental.exception.ErrorMessage.*;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardRequest;
import com.example.SSU_Rental.common.BaseEntity;
import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.exception.ErrorMessage;
import com.example.SSU_Rental.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Boardrp extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "boardrp_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // -> 댓글 여러개에 board 1개
    @JoinColumn(name = "board_id")
    private Board board;


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

    public static Boardrp createBoardrp(Board board,Member member, BoardrpRequest boardrpRequest){
        return Boardrp.builder()
            .board(board)
            .member(member)
            .content(boardrpRequest.getContent())
            .build();
    }

    public void validate(Member member,Board board){


        if(this.member.getId()!=member.getId()){
            throw new CustomException((FORBIDDEN_ERROR));
        }

        if(this.board.getId()!=board.getId()){
            throw new IllegalArgumentException("댓글 아이디 혹은 게시글 아이디가 잘못되었습니다.");
        }
    }

    public void modify(String content) {
        this.content = content;
    }
}
