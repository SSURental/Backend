package com.example.SSU_Rental.board;

import com.example.SSU_Rental.boardrp.Boardrp;
import com.example.SSU_Rental.boardrp.BoardrpResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardResponse {


    @Schema(description = "게시글 아이디")
    private Long id;

    @Schema(description = "게시글 작성자 이름")
    private String nickname;

    @Schema(description = "게시글 제목")
    private String title;

    @Schema(description = "게시글 내용")
    private String content;

    @Schema(description = "게시글 조회수")
    private int views; // 조회수

    @Schema(description = "게시글 좋아요수")
    private int likes; // 추천수

    @Schema(description = "게시글 싫어요수")
    private int dislikes; // 추천수

    @Schema(description = "게시글 신고받은 횟수")
    private int warns; // 신고받은횟수

    @Schema(description = "게시글 작성 날짜")
    private LocalDateTime createdDate;

    @Schema(description = "게시글 수정 날짜")
    private LocalDateTime lastmodifiedDate;

    @Builder
    public BoardResponse(Long id, String nickname, String title, String content, int views,
        int likes,
        int dislikes, int warns, LocalDateTime createdDate, LocalDateTime lastmodifiedDate) {
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.views = views;
        this.likes = likes;
        this.dislikes = dislikes;
        this.warns = warns;
        this.createdDate = createdDate;
        this.lastmodifiedDate = lastmodifiedDate;
    }



    public static BoardResponse from(Board entity) {
        return new BoardResponse(entity.getId(), entity.getMember().getName(), entity.getTitle(),
            entity.getContent(), entity.getViews(), entity.getLikes(), entity.getDislikes(),entity.getWarns(),
            entity.getRegDate(), entity.getModDate());

    }
}
