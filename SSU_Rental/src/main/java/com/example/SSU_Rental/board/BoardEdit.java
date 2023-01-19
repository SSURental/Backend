package com.example.SSU_Rental.board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardEdit {


    private String title;

    private String content;


    @Builder
    public BoardEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
