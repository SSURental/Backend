package com.example.SSU_Rental.board;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardResponse {


    private Long id;

    private String nickname;

    private String title;

    private String content;

    private int views; // 조회수

    private int likes; // 추천수

    private int dislikes; // 추천수

    private int warns; // 신고받은횟수

    private LocalDateTime createdDate;

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
