package com.example.SSU_Rental.boardrp;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardrpResponse {

    private String content;

    private String nickname;


    public BoardrpResponse(String content, String nickname) {
        this.content = content;
        this.nickname = nickname;
    }

    public static BoardrpResponse from(Boardrp entity) {
        return new BoardrpResponse(entity.getContent(),entity.getMember().getName());

    }
}
