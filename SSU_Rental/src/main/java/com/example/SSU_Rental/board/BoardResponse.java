package com.example.SSU_Rental.board;

import com.example.SSU_Rental.boardrp.Boardrp;
import com.example.SSU_Rental.boardrp.BoardrpResponse;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardResponse {


    private Long id;

    private String nickname;

    private String title;

    private String content;

    private int see_cnt; // 조회수

    private int rec_cnt; // 추천수

    private int warn_cnt; // 신고받은횟수

    private LocalDateTime createdDate;

    private LocalDateTime lastmodifiedDate;

    @Builder
    public BoardResponse(Long id, String nickname, String title, String content, int see_cnt,
        int rec_cnt, int warn_cnt, LocalDateTime createdDate, LocalDateTime lastmodifiedDate) {
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.see_cnt = see_cnt;
        this.rec_cnt = rec_cnt;
        this.warn_cnt = warn_cnt;
        this.createdDate = createdDate;
        this.lastmodifiedDate = lastmodifiedDate;
    }

    public static BoardResponse from(Board entity) {
        return new BoardResponse(entity.getId(), entity.getMember().getName(), entity.getTitle(),
            entity.getContent(), entity.getSee_cnt(), entity.getRec_cnt(), entity.getWarn_cnt(),
            entity.getRegDate(), entity.getModDate());

    }
}
