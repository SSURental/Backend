package com.example.SSU_Rental.boardrp;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardrpResponse {


    private String content;

    private String nickname;

    private LocalDateTime createdDate;

    private LocalDateTime lastmodifiedDate;

    public static BoardrpResponse from(Boardrp entity) {
        return new BoardrpResponse(entity.getContent(),entity.getMember().getName(),entity.getRegDate(),entity.getModDate());

    }
}
