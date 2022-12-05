package com.example.SSU_Rental.item;

import com.example.SSU_Rental.image.ImageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
public class ItemRequest {


    @Schema(description = "아이템 이름")
    private String itemName;

    @Schema(description = "아이템 가겨")
    private int price;

    @Schema(description = "아이템 관련 사진 URL 모음")
    private List<ImageDTO> imageDTOList = new ArrayList<>();

}
