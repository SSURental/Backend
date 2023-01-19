package com.example.SSU_Rental.item;

import com.example.SSU_Rental.image.ImageDTO;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemRequest {


    @NotBlank(message = "아이템 이름은 공백이 될 수 없습니다.")
    private String itemName;

    @NotNull(message="가격을 입력해 주세요.")
    @Min(value = 1000,message = "최소 가격은 1000원 이상이어야 합니다.")
    private int price;

    private List<ImageDTO> imageDTOList = new ArrayList<>();

    @Builder
    public ItemRequest(String itemName, int price) {
        this.itemName = itemName;
        this.price = price;

    }
}
