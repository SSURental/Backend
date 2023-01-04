package com.example.SSU_Rental.rating;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RatingEditor {

    private String content;

    private Integer score;

    @Builder
    public RatingEditor(String content, Integer score) {
        this.content = content;
        this.score = score;
    }

    public static class RatingEditorBuilder {
        private String content;
        private Integer score;

        RatingEditorBuilder() {
        }

        public RatingEditor.RatingEditorBuilder content(final String content) {
            if(!content.equals("")){
                this.content = content;
            }
            return this;
        }

        public RatingEditor.RatingEditorBuilder score(final Integer score) {
            if(score!=null&&score!=0){
                if(score>10||score<0){
                    throw new IllegalArgumentException("점수는 1~10점 사이만 가능합니다.");
                }
                this.score = score;
            }
            return this;
        }

        public RatingEditor build() {
            return new RatingEditor(this.content, this.score);
        }

        public String toString() {
            return "RatingEditor.RatingEditorBuilder(content=" + this.content + ", score=" + this.score + ")";
        }
    }
}
