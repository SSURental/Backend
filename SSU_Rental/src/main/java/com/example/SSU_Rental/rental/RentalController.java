package com.example.SSU_Rental.rental;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.login.UserSession;
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
    public ResponseEntity<Long> rental(
        @PathVariable Long itemId,
        UserSession session) {
        Long rentalId = rentalService.rental(itemId, session);
        return ResponseEntity.created(URI.create("/item/" + itemId + "/" + rentalId))
            .body(rentalId);
    }


    @GetMapping("/items/{itemId}/rentals/{rentalId}")
    public ResponseEntity<RentalResponse> getOne(
        @PathVariable Long itemId,
        @PathVariable Long rentalId) {
        return ResponseEntity.ok().body(rentalService.getOne(itemId, rentalId));
    }

    /**
     * 내가 대여한 리스트 목록 가져오기( 타인의 대여 리스트는 굳이 알 필요X)
     */

    @GetMapping("/rentals")
    public ResponseEntity<ResponsePageDTO> getList(
        UserSession session,
        RequestPageDTO requestPageDTO) {
        return ResponseEntity.ok()
            .body(rentalService.getList(requestPageDTO,session));
    }

    /**
     * 대여 기한 일주일 연장
     */


    @PatchMapping("/items/{itemId}/rentals/{rentalId}")
    public ResponseEntity<RentalResponse> extendRental(
        @PathVariable Long itemId,
        @PathVariable Long rentalId,
        UserSession session) {
        return ResponseEntity.ok()
            .body(rentalService.extendRental(itemId, rentalId, session));
    }


    @DeleteMapping("/items/{itemId}/rentals/{rentalId}")
    public ResponseEntity returnItem(
        @PathVariable Long itemId,
        @PathVariable Long rentalId,
        UserSession session) {
        rentalService.returnItem(itemId, rentalId, session);
        return ResponseEntity.ok().build();
    }
}
