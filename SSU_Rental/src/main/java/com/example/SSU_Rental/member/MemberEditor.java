package com.example.SSU_Rental.member;

import com.example.SSU_Rental.image.MemberImage;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberEditor {

    private String name;
    private MemberImage memberImage;

    @Builder
    public MemberEditor(String name, MemberImage memberImage) {
        this.name = name;
        this.memberImage = memberImage;
    }


    public static class MemberEditorBuilder {
        private String name;
        private MemberImage memberImage;

        MemberEditorBuilder() {
        }

        public MemberEditor.MemberEditorBuilder name(final String name) {
            if(!name.equals("")){
                this.name = name;
            }
            return this;
        }

        public MemberEditor.MemberEditorBuilder memberImage(final MemberImage memberImage) {
            if(!memberImage.getImgName().equals("")){
                this.memberImage = memberImage;
            }
            return this;
        }

        public MemberEditor build() {
            return new MemberEditor(this.name, this.memberImage);
        }

        public String toString() {
            return "MemberEditor.MemberEditorBuilder(name=" + this.name + ", memberImage=" + this.memberImage + ")";
        }
    }
}
