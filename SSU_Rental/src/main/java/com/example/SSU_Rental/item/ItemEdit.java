package com.example.SSU_Rental.item;

import com.example.SSU_Rental.image.ImageDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemEdit {

    private String itemName;

    private Integer price;

    private List<ImageDTO> imageDTOList = new ArrayList<>();


    @Builder
    public ItemEdit(String itemName, Integer price) {
        this.itemName = itemName;
        this.price = price;
    }
}
