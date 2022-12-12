package com.example.SSU_Rental.item;

import com.example.SSU_Rental.image.ImageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
public class ItemRequest {


    @NotBlank(message = "아이템 이름은 공백이 될 수 없습니다.")
    @Schema(description = "아이템 이름")
    private String itemName;

    @NotNull(message="가격을 입력해 주세요.")
    @Schema(description = "아이템 가격")
    private int price;

    @Schema(description = "아이템 관련 사진 URL 모음")
    private List<ImageDTO> imageDTOList = new ArrayList<>();

}
