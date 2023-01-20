package com.example.SSU_Rental.boardrp;


import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.boardrp.BoardrpEditor.BoardrpEditorBuilder;
import com.example.SSU_Rental.common.BaseEntity;
import com.example.SSU_Rental.exception.BadRequestException;
import com.example.SSU_Rental.exception.ForbiddenException;
import com.example.SSU_Rental.exception.notfound.BoardrpNotFound;
import com.example.SSU_Rental.member.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private boolean isDeleted;


    @Builder
    public Boardrp(Long id, Board board, Member member, String content,boolean isDeleted) {
        this.id = id;
        this.board = board;
        this.member = member;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public static Boardrp createBoardrp(Board board,Member member, BoardrpRequest boardrpRequest){
        Boardrp boardrp = Boardrp.builder()
            .board(board)
            .member(member)
            .content(boardrpRequest.getContent())
            .isDeleted(false)
            .build();
        board.addBoard(boardrp);
        return boardrp;
    }

    private void validate(Member member,Board board){

        if(this.member.getId()!=member.getId()){
            throw new ForbiddenException();
        }

        if(this.board.getId()!=board.getId()){
            throw new BoardrpNotFound();
        }
    }


    public BoardrpEditorBuilder  toEditor() {
        return BoardrpEditor.builder()
            .content(this.content);
    }

    public void edit(BoardrpEditor editor,Member loginMember,Board board){
        validate(loginMember,board);
        this.content = editor.getContent();
    }

    public void delete(Member loginMember,Board board){
        validate(loginMember,board);
        this.isDeleted = true;
    }
}
