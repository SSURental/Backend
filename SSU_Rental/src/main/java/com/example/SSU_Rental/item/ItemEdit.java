package com.example.SSU_Rental.item;

import com.example.SSU_Rental.image.ImageDTO;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemEdit {

    private String itemName;

    private int price;

    private List<ImageDTO> imageDTOList = new ArrayList<>();


    @Builder
    public ItemEdit(String itemName, int price,
        List<ImageDTO> imageDTOList) {
        this.itemName = itemName;
        this.price = price;
        this.imageDTOList = imageDTOList;
    }
}
