package com.example.SSU_Rental.item;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.image.ImageDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemResponse {


    private Long id;

    private String itemName;

    private Group group;

    private ItemStatus status;


    private String memberName;

    private int price;

    @Builder.Default
    private List<ImageDTO> imageDTOList = new ArrayList<>();

    @Builder
    public ItemResponse(Long id, String itemName, Group group,
        ItemStatus status, String memberName, int price) {
        this.id = id;
        this.itemName = itemName;
        this.group = group;
        this.status = status;
        this.memberName = memberName;
        this.price = price;
    }

    private void addImage(ImageDTO imageDTO){
        this.imageDTOList.add(imageDTO);
    }



    public static ItemResponse from(Item item) {
        ItemResponse itemResponse = ItemResponse.builder()
            .id(item.getId())
            .itemName(item.getItemName())
            .group(item.getItemGroup())
            .status(item.getStatus())
            .memberName(item.getMember().getName())
            .price(item.getPrice())
            .build();

        List<ImageDTO> imageDTOS = item.getItemImages().stream().map(itemImage -> {
            return new ImageDTO(itemImage.getImgName());
        }).collect(Collectors.toList());

       itemResponse.setImageDTOList(imageDTOS);
        return itemResponse;

    }

    private void setImageDTOList(List<ImageDTO> list){
        this.imageDTOList = list;
    }
}
