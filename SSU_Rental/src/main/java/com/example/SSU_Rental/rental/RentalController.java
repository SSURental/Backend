package com.example.SSU_Rental.rental;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.member.AuthMember;
import com.example.SSU_Rental.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;



    @PostMapping("/items/{itemId}/rentals")
    public ResponseEntity<Long> rental(@PathVariable Long itemId,@AuthMember Member member){
        Long rentalId = rentalService.rental(itemId, member.getId());
        return ResponseEntity.created(URI.create("/item/"+itemId +"/"+ rentalId)).body(rentalId);
    }

    @GetMapping("/items/{itemId}/rentals/{rentalId}")
    public ResponseEntity<RentalResponse> getOne(@PathVariable Long itemId,@PathVariable Long rentalId){
        return ResponseEntity.ok().body(rentalService.getOne(rentalId));
    }

    /**
     * 내가 대여한 리스트 목록 가져오기( 타인의 대여 리스트는 굳이 알 필요X)
     */
    @GetMapping("/rentals")
    public ResponseEntity<ResponsePageDTO> getMyRentalList(@AuthMember Member member,RequestPageDTO requestPageDTO){
        return ResponseEntity.ok().body(rentalService.getMyRentalList(member.getId(), requestPageDTO));
    }

    /**
     * 대여 기한 연장
     */

    @PatchMapping("/rentals/{rentalId}")
    public ResponseEntity<RentalResponse> extendRental(@PathVariable Long itemId,@PathVariable Long rentalId,@AuthMember Member member){
        return ResponseEntity.ok().body(rentalService.extendRental(itemId,rentalId, member.getId()));
    }

    @DeleteMapping("/items/{itemId}/rentals/{rentalId}")
    public ResponseEntity returnItem(@PathVariable Long itemId,@PathVariable Long rentalId, @AuthMember Member member){
        rentalService.returnItem(itemId,rentalId ,member.getId());
        return ResponseEntity.ok().build();
    }
}
