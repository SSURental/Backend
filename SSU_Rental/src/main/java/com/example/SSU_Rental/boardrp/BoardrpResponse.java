package com.example.SSU_Rental.boardrp;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardrpResponse {


    private Long id;

    private String content;

    private String nickname;

    private Long boardId;

    private LocalDateTime createdDate;

    private LocalDateTime lastmodifiedDate;

    public static BoardrpResponse from(Boardrp entity) {
        return new BoardrpResponse(entity.getId(), entity.getContent(), entity.getMember().getName(),entity.getBoard().getId(),
            entity.getRegDate(), entity.getModDate());

    }
}
