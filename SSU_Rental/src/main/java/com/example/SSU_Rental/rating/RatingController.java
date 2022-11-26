package com.example.SSU_Rental.rating;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/members/{member_id}/ratings")
    public ResponseEntity<Long> register(@PathVariable Long member_id,
        @RequestBody RatingRequest ratingRequest) {
        Long registerId = ratingService.register(member_id, ratingRequest);
        return ResponseEntity.created(URI.create("/member/" + member_id + "/ratings/" + registerId))
            .body(registerId);

    }

    @GetMapping("/members/{member_id}/ratings/scores")
    public ResponseEntity<Integer> getAvgScores(@PathVariable Long member_id) {
        return ResponseEntity.ok().body(ratingService.getAvgScores(member_id));
    }

    @GetMapping("/members/{member_id}/ratings")
    public ResponseEntity<ResponsePageDTO> getRatingList(@PathVariable Long member_id,
        RequestPageDTO requestPageDTO) {
        ResponsePageDTO responsePage = ratingService.getRatingList(member_id, requestPageDTO);
        return ResponseEntity.ok().body(responsePage);
    }

}
