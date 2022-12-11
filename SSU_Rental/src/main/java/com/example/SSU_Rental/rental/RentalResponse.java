package com.example.SSU_Rental.rental;
import com.example.SSU_Rental.image.ImageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RentalResponse {

    @Schema(description = "렌탈 아이디")
    private Long id;

    @Schema(description = "빌리는 사람 아이디")
    private String nickname;

    private String itemName;

    private ImageDTO imageDTO;

    @Schema(description = "빌린 날짜")
    private LocalDateTime startDate;

    @Schema(description = "반납 날짜")
    private LocalDateTime endDate;


    @Builder
    public RentalResponse(Long id, String nickname, String itemName,ImageDTO imageDTO,LocalDateTime startDate,
        LocalDateTime endDate) {
        this.id = id;
        this.nickname = nickname;
        this.itemName = itemName;
        this.imageDTO = imageDTO;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static RentalResponse from(Rental rental){
        return RentalResponse.builder()
            .id(rental.getId())
            .nickname(rental.getMember().getName())
            .itemName(rental.getItem().getItemName())
            .imageDTO(new ImageDTO(rental.getItem().getItemImages().get(0).getImgName()))
            .startDate(rental.getStartDate())
            .endDate(rental.getEndDate())
            .build();
    }
}
