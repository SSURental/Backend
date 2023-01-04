package com.example.SSU_Rental.boardrp;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardrpEdit {

    private String content;

    @Builder
    public BoardrpEdit(String content) {
        this.content = content;
    }


}
