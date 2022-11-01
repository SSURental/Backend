package com.example.SSU_Rental.board;

import com.example.SSU_Rental.member.Member;
import lombok.Builder;
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
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @Column
    @ManyToOne(fetch = FetchType.LAZY)  //다대일 관계 -> 게시글 여러개에 member 1
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String content;

    private Long see_cnt; // 조회수

    private Long rec_cnt; // 추천수

    private Long warn_cnt; // 신고받은횟수


    @Builder
    public Board(Long id, Member member, String title, String content, Long see_cnt,
        Long rec_cnt, Long warn_cnt) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
        this.see_cnt = see_cnt;
        this.rec_cnt = rec_cnt;
        this.warn_cnt = warn_cnt;
    }


    public void view(){
        see_cnt+=1;
    }

    public void recommend(){rec_cnt+=1;}

    public void warn(){warn_cnt+=1;}

    public void modify(String title, String content){
        this.title = title;
        this.content = content;
    }

    public BoardResponse makeResponse(){
        return BoardResponse
            .builder()
            .id(id)
            .nickname(member.getName())
            .title(title)
            .content(content)
            .see_cnt(see_cnt)
            .rec_cnt(rec_cnt)
            .warn_cnt(warn_cnt)
            .build();
    }
}
