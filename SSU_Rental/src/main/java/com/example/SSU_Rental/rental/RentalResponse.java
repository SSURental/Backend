package com.example.SSU_Rental.rental;
import com.example.SSU_Rental.image.ImageDTO;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RentalResponse {

    private Long id;

    private String nickname;

    private Long itemId;

    private String itemName;

    private ImageDTO imageDTO;

    private LocalDateTime startDate;

    private LocalDateTime endDate;


    @Builder
    public RentalResponse(Long id, String nickname,Long itemId ,String itemName,ImageDTO imageDTO,LocalDateTime startDate,
        LocalDateTime endDate) {
        this.id = id;
        this.nickname = nickname;
        this.itemId = itemId;
        this.itemName = itemName;
        this.imageDTO = imageDTO;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static RentalResponse from(Rental rental){
        return RentalResponse.builder()
            .id(rental.getId())
            .nickname(rental.getMember().getName())
            .itemId(rental.getItem().getId())
            .itemName(rental.getItem().getItemName())
            .imageDTO(new ImageDTO(rental.getItem().getItemImages().get(0).getImgName()))
            .startDate(rental.getStartDate())
            .endDate(rental.getEndDate())
            .build();
    }
}
