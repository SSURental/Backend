package com.example.SSU_Rental.item;

import com.example.SSU_Rental.image.ImageDTO;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@AllArgsConstructor
public class ItemRequest {


    @NotBlank(message = "아이템 이름은 공백이 될 수 없습니다.")
    private String itemName;

    @NotNull(message="가격을 입력해 주세요.")
    private int price;

    private List<ImageDTO> imageDTOList = new ArrayList<>();

}
