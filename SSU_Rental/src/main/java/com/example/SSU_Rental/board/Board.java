package com.example.SSU_Rental.board;

import com.example.SSU_Rental.board.BoardEditor.BoardEditorBuilder;
import com.example.SSU_Rental.boardrp.Boardrp;
import com.example.SSU_Rental.boardrp.BoardrpRequest;
import com.example.SSU_Rental.common.BaseEntity;
import com.example.SSU_Rental.exception.ForbiddenException;
import com.example.SSU_Rental.member.Member;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  //다대일 관계 -> 게시글 여러개에 member 1
    @JoinColumn(name = "member_id")
    private Member member;


    private String title;

    private String content;

    @OneToMany( cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY,
        mappedBy = "board")
    private List<Boardrp> boardrpList = new ArrayList<>();


    private int views; // 조회수

    private int likes; // 추천수

    private int dislikes; // 싫어요

    private int warns; // 신고받은횟수

    private boolean blocked; // 신고 받아서 차단

    private boolean isDeleted; // 삭제 된 글인지 여부


    @Builder
    public Board(Long id, Member member, String title, String content, int views, int likes,
        int dislikes, int warns,boolean blocked,boolean isDeleted) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
        this.views = views;
        this.likes = likes;
        this.dislikes = dislikes;
        this.warns = warns;
        this.blocked = blocked;
        this.isDeleted = isDeleted;
    }



    public static Board createBoard(BoardRequest boardRequest, Member member) {
        return Board.builder()
            .title(boardRequest.getTitle())
            .content(boardRequest.getContent())
            .member(member)
            .views(0)
            .likes(0)
            .dislikes(0)
            .warns(0)
            .blocked(false)
            .isDeleted(false)
            .build();
    }

    public void view() {
        this.views ++;
    }

    public void like() {this.likes ++;}

    public void dislike(){this.dislikes++;}

    public void warn() {
        this.warns ++;
        if(this.warns>10){
         this.blocked = true;
        }
    }

    public void addBoard(Boardrp boardrp){
        this.boardrpList.add(boardrp);
    }

    private void validate(Member member){
        if(this.member.getId()!=member.getId()){
            throw new ForbiddenException();
        }
    }

    public BoardEditorBuilder toEditor(){
        return BoardEditor
            .builder()
            .title(title)
            .content(content);
    }

    public void edit(BoardEditor editor,Member loginMember) {
        validate(loginMember);
        this.title = editor.getTitle();
        this.content = editor.getContent();

    }

    public void delete(Member loginMember){
        validate(loginMember);
        this.isDeleted = true;
    }


    public Boardrp createBoardrp(Member loginMember, BoardrpRequest boardrpRequest) {
        Boardrp boardrp = Boardrp.createBoardrp(this, loginMember, boardrpRequest);
        this.boardrpList.add(boardrp);
        return boardrp;
    }
}
