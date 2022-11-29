package com.example.SSU_Rental.item;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.image.ItemImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemResponse {


    private Long id;

    private String itemName;

    private Group group;

    private ItemStatus status;

    private String item_owner;

    private int price;


    @Builder.Default
    private List<ImageDTO> imageDTOList = new ArrayList<>();

    @Builder
    public ItemResponse(Long id, String itemName, Group group,
        ItemStatus status, String item_owner, int price) {
        this.id = id;
        this.itemName = itemName;
        this.group = group;
        this.status = status;
        this.item_owner = item_owner;
        this.price = price;
    }

    private void addImage(ImageDTO imageDTO){
        this.imageDTOList.add(imageDTO);
    }


    public static ItemResponse from(Item item) {
        return ItemResponse.builder()
            .id(item.getId())
            .itemName(item.getItem_name())
            .group(item.getItem_group())
            .status(item.getItem_status())
            .item_owner(item.getItem_owner().getName())
            .price(item.getPrice())
            .build();
    }

    public static ItemResponse from(Item item, List<ItemImage> itemImages) {
        ItemResponse itemResponse = ItemResponse.builder()
            .id(item.getId())
            .itemName(item.getItem_name())
            .group(item.getItem_group())
            .status(item.getItem_status())
            .item_owner(item.getItem_owner().getName())
            .price(item.getPrice())
            .build();

        List<ImageDTO> imageDTOS = itemImages.stream().map(itemImage -> {
            return new ImageDTO(itemImage.getImgName(), itemImage.getUuid(), itemImage.getPath());
        }).collect(Collectors.toList());

        for (ImageDTO imageDTO : imageDTOS) {
            itemResponse.addImage(imageDTO);
        }

        return itemResponse;

    }
}
