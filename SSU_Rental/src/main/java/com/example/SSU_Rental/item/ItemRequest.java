package com.example.SSU_Rental.item;

import com.example.SSU_Rental.image.ImageDTO;
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


    private String name;

    private int price;

    @Builder.Default
    private List<ImageDTO> imageDTOList = new ArrayList<>();

}
