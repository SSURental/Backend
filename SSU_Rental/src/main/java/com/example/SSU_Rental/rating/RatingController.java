package com.example.SSU_Rental.rating;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "리뷰",description = "리뷰 API")
@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;


    @Operation(summary = "리뷰 작성 요청",parameters={@Parameter(in = ParameterIn.HEADER,name = "X-AUTH_TOKEN",required = true,description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",description = "리뷰 작성 성공", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PostMapping("/members/{member_id}/ratings")
    public ResponseEntity<Long> register(@Parameter(description = "리뷰받을 멤버 ID",required = true) @PathVariable Long member_id,
        @RequestBody RatingRequest ratingRequest) {
        Long registerId = ratingService.register(member_id, ratingRequest);
        return ResponseEntity.created(URI.create("/member/" + member_id + "/ratings/" + registerId))
            .body(registerId);

    }

    @Operation(summary = "특정 멤버가 받은 리뷰의 평균 점수 요청")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "리뷰 평균 점수 받기 성공", content = @Content(schema = @Schema(implementation = Double.class)))
    })
    @GetMapping("/members/{member_id}/ratings/scores")
    public ResponseEntity<Double> getAvgScores(@Parameter(description = "리뷰받은 멤버 ID",required = true) @PathVariable Long member_id) {
        return ResponseEntity.ok().body(ratingService.getAvgScores(member_id));
    }

    @Operation(summary = "특정 멤버가 받은 리뷰의 목록 요청")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "특정 멤버가 받은 리뷰의 목록 가져오기 성공", content = @Content(schema = @Schema(implementation = ResponsePageDTO.class)))
    })
    @GetMapping("/members/{member_id}/ratings")
    public ResponseEntity<ResponsePageDTO> getRatingList(@Parameter(description = "리뷰받은 멤버 ID",required = true) @PathVariable Long member_id,
        @ParameterObject RequestPageDTO requestPageDTO) {
        ResponsePageDTO responsePage = ratingService.getRatingList(member_id, requestPageDTO);
        return ResponseEntity.ok().body(responsePage);
    }

}
