package com.example.SSU_Rental.board;

import com.example.SSU_Rental.common.BaseEntity;
import com.example.SSU_Rental.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private int see_cnt; // 조회수

    private int rec_cnt; // 추천수

    private int warn_cnt; // 신고받은횟수


    @Builder
    public Board(Long id, Member member, String title, String content, int see_cnt,
        int rec_cnt, int warn_cnt) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
        this.see_cnt = see_cnt;
        this.rec_cnt = rec_cnt;
        this.warn_cnt = warn_cnt;
    }


    public static Board makeBoardOne(String title, String content, Member member) {
        return Board.builder()
            .title(title)
            .content(content)
            .member(member)
            .see_cnt(0)
            .rec_cnt(0)
            .warn_cnt(0)
            .build();
    }

    public void view() {
        see_cnt += 1;
    }

    public void recommend() {
        rec_cnt += 1;
    }

    public void warn() {
        warn_cnt += 1;
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
    }


}
