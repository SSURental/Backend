package com.example.SSU_Rental.boardrp;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BoardrpEditor {


    private String content;

    @Builder
    public BoardrpEditor(String content) {
        this.content = content;
    }

    public static class BoardrpEditorBuilder {
        private String content;

        BoardrpEditorBuilder() {
        }

        public BoardrpEditor.BoardrpEditorBuilder content(final String content) {
            if(!content.equals("")){
                this.content = content;
            }
            return this;
        }

        public BoardrpEditor build() {
            return new BoardrpEditor(this.content);
        }

        public String toString() {
            return "BoardrpEditor.BoardrpEditorBuilder(content=" + this.content + ")";
        }
    }
}
