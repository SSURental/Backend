package com.example.SSU_Rental.rating;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.login.UserSession;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;


    @PostMapping("/items/{itemId}/ratings")
    public ResponseEntity<Long> register(
        @PathVariable Long itemId,
        @Validated @RequestBody RatingRequest ratingRequest,
        UserSession session) {
        Long registerId = ratingService.register(itemId, ratingRequest, session);
        return ResponseEntity.created(URI.create("/member/" + itemId + "/ratings/" + registerId))
            .body(registerId);

    }


    @GetMapping("/items/{itemId}/ratings/scores")

    public ResponseEntity<Double> getAvgScores(
        @PathVariable Long itemId) {
        return ResponseEntity.ok().body(ratingService.getAvgScores(itemId));
    }


    @GetMapping("/items/{itemId}/ratings")
    public ResponseEntity<ResponsePageDTO> getRatingList(@PathVariable Long itemId,
        RequestPageDTO requestPageDTO) {
        ResponsePageDTO responsePage = ratingService.getList(itemId, requestPageDTO);
        return ResponseEntity.ok().body(responsePage);
    }


    @PatchMapping("/items/{itemId}/ratings/{ratingId}")
    public ResponseEntity<ResponsePageDTO> edit(
        @PathVariable Long itemId,
        @PathVariable Long ratingId,
        @RequestBody RatingEdit editRequest, UserSession session) {
        ratingService.edit(itemId, ratingId, editRequest, session);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/items/{itemId}/ratings/{ratingId}")
    public ResponseEntity<ResponsePageDTO> delete(
        @PathVariable Long itemId,
        @PathVariable Long ratingId,
        UserSession session) {
        ratingService.remove(itemId, ratingId, session);
        return ResponseEntity.ok().build();
    }

}
