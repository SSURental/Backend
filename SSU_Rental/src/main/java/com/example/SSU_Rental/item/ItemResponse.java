package com.example.SSU_Rental.item;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.image.ItemImage;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemResponse {


    @Schema(description = "아이템 아이디")
    private Long id;

    @Schema(description = "아이템 이름")
    private String itemName;

    @Schema(description = "아이템이 속한 그룹",example = "SCHOOL")
    private Group group;

    @Schema(description = "아이템 상태(렌털가능한지 불가능한지..)",example = "AVAILABLE")
    private ItemStatus status;


    @Schema(description = "아이템 주인 이름")
    private String memberName;

    @Schema(description = "렌탈에 필요한 가격")
    private int price;

    @Schema(description = "아이템 관련 사진 URL 모음")
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
        return ItemResponse.builder()
            .id(item.getId())
            .itemName(item.getItemName())
            .group(item.getItemGroup())
            .status(item.getStatus())
            .memberName(item.getMember().getName())
            .price(item.getPrice())
            .build();
    }

    public static ItemResponse from(Item item, List<ItemImage> itemImages) {
        ItemResponse itemResponse = ItemResponse.builder()
            .id(item.getId())
            .itemName(item.getItemName())
            .group(item.getItemGroup())
            .status(item.getStatus())
            .memberName(item.getMember().getName())
            .price(item.getPrice())
            .build();

        List<ImageDTO> imageDTOS = itemImages.stream().map(itemImage -> {
            return new ImageDTO(itemImage.getImgName());
        }).collect(Collectors.toList());

       itemResponse.setImageDTOList(imageDTOS);
        return itemResponse;

    }

    private void setImageDTOList(List<ImageDTO> list){
        this.imageDTOList = list;
    }
}
