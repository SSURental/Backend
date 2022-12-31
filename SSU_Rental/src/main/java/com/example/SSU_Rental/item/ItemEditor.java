package com.example.SSU_Rental.item;

import com.example.SSU_Rental.image.ItemImage;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ItemEditor {

    private String itemName;
    private Integer price;
    private List<ItemImage> itemImages = new ArrayList<>();

    @Builder
    public ItemEditor(String itemName, Integer price,
        List<ItemImage> itemImages) {
        this.itemName = itemName;
        this.price = price;
        this.itemImages = itemImages;
    }

    public static class ItemEditorBuilder {
        private String itemName;
        private Integer price;
        private List<ItemImage> itemImages = new ArrayList<>();

        ItemEditorBuilder() {
        }

        public ItemEditor.ItemEditorBuilder itemName(final String itemName) {
            if(!itemName.equals("")) {
                this.itemName = itemName;
            }
            return this;
        }

        public ItemEditor.ItemEditorBuilder price(final Integer price) {
            if(price!=null&&price!=0){
                this.price = price;
            }
            return this;
        }

        public ItemEditor.ItemEditorBuilder itemImages(final List<ItemImage> itemImages) {
            if(itemImages.size()!=0){
                this.itemImages.clear();
                this.itemImages.addAll(itemImages);
            }
            return this;
        }

        public ItemEditor build() {
            return new ItemEditor(this.itemName, this.price, this.itemImages);
        }

        public String toString() {
            return "ItemEditor.ItemEditorBuilder(itemName=" + this.itemName + ", price=" + this.price + ", itemImages=" + this.itemImages + ")";
        }
    }
}
