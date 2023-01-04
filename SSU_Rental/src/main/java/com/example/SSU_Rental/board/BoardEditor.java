package com.example.SSU_Rental.board;


import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardEditor {

    private String title;

    private String content;

    @Builder
    public BoardEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static class BoardEditorBuilder {
        private String title;
        private String content;

        BoardEditorBuilder() {
        }

        public BoardEditor.BoardEditorBuilder title(final String title) {
            if(!title.equals("")){
                this.title = title;
            }
            return this;
        }

        public BoardEditor.BoardEditorBuilder content(final String content) {
            if(!content.equals("")){
                this.content = content;
            }
            return this;
        }

        public BoardEditor build() {
            return new BoardEditor(this.title, this.content);
        }

        public String toString() {
            return "BoardEditor.BoardEditorBuilder(title=" + this.title + ", content=" + this.content + ")";
        }
    }
}
