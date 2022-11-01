package com.example.SSU_Rental.board;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardResponse {


    private Long id;

    private String nickname;

    private String title;

    private String content;

    private Long see_cnt; // 조회수

    private Long rec_cnt; // 추천수

    private Long warn_cnt; // 신고받은횟수


    @Builder
    public BoardResponse(Long id, String nickname, String title, String content, Long see_cnt,
        Long rec_cnt, Long warn_cnt) {
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.see_cnt = see_cnt;
        this.rec_cnt = rec_cnt;
        this.warn_cnt = warn_cnt;
    }

    public static BoardResponse from(Board entity) {
        return BoardResponse.builder()
            .id(entity.getId())
            .nickname(entity.getMember().getName())
            .title(entity.getTitle())
            .content(entity.getContent())
            .see_cnt(entity.getSee_cnt())
            .rec_cnt(entity.getRec_cnt())
            .warn_cnt(entity.getWarn_cnt())
            .build();

    }
}
