package com.example.SSU_Rental.boardrp;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardrpResponse {


    @Schema(description = "게시글 관련 댓글 내용")
    private String content;

    @Schema(description = "댓글 작성자")
    private String nickname;

    @Schema(description = "댓글 작성 날짜")
    private LocalDateTime createdDate;

    @Schema(description = "댓글 수정 날짜")
    private LocalDateTime lastmodifiedDate;

    public static BoardrpResponse from(Boardrp entity) {
        return new BoardrpResponse(entity.getContent(), entity.getMember().getName(),
            entity.getRegDate(), entity.getModDate());

    }
}
