package com.example.SSU_Rental.rating;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.config.LogExecutionTime;
import com.example.SSU_Rental.exception.ErrorResponseDTO;
import com.example.SSU_Rental.member.AuthMember;
import com.example.SSU_Rental.member.Member;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "리뷰", description = "리뷰 API")
@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;


    @Operation(summary = "리뷰 작성 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "리뷰 작성 성공", content = @Content(schema = @Schema(implementation = Long.class))),
        @ApiResponse(responseCode = "404",description = "아이템 아이디가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/items/{itemId}/ratings")
    @LogExecutionTime
    public ResponseEntity<Long> register(
        @Parameter(description = "리뷰 받을 아이템 ID", required = true) @PathVariable Long itemId,
        @Validated @RequestBody RatingRequest ratingRequest,
        @Parameter(hidden = true) @AuthMember Member member) {
        Long registerId = ratingService.register(itemId, ratingRequest, member.getId());
        return ResponseEntity.created(URI.create("/member/" + itemId + "/ratings/" + registerId))
            .body(registerId);

    }

    @Operation(summary = "특정 아이템이 받은 리뷰의 평균 점수 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 평균 점수 받기 성공", content = @Content(schema = @Schema(implementation = Double.class))),
        @ApiResponse(responseCode = "404",description = "아이템 아이디가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "400",description = "리뷰받은 적이 없는 아이템",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/items/{itemId}/ratings/scores")
    @LogExecutionTime
    public ResponseEntity<Double> getAvgScores(
        @Parameter(description = "리뷰 받을 아이템 ID", required = true) @PathVariable Long itemId) {
        return ResponseEntity.ok().body(ratingService.getAvgScores(itemId));
    }

    @Operation(summary = "특정 아이템이 받은 리뷰의 목록 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "특정 멤버가 받은 리뷰의 목록 가져오기 성공", content = @Content(schema = @Schema(implementation = ResponsePageDTO.class))),
        @ApiResponse(responseCode = "404",description = "아이템 아이디가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/items/{itemId}/ratings")
    @LogExecutionTime
    public ResponseEntity<ResponsePageDTO> getRatingList(
        @Parameter(description = "리뷰 받을 아이템 ID", required = true) @PathVariable Long itemId,
        @ParameterObject RequestPageDTO requestPageDTO) {
        ResponsePageDTO responsePage = ratingService.getRentalList(itemId, requestPageDTO);
        return ResponseEntity.ok().body(responsePage);
    }

//    사용하지 않은 기능 삭제
//    @Operation(summary = "특정 리뷰 수정")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "특정 리뷰 수정 성공")
//    })
//    @PatchMapping("/items/{itemId}/ratings/{ratingId}")
//    public ResponseEntity<ResponsePageDTO> modify(
//        @Parameter(description = "리뷰 받을 아이템 ID", required = true) @PathVariable Long itemId,
//        @Parameter(description = "리뷰 ID", required = true) @PathVariable Long ratingId,
//        @RequestBody RatingRequest ratingRequest, @AuthMember Member member) {
//        ratingService.modify(itemId,ratingId, member.getId(), ratingRequest);
//        return ResponseEntity.ok().build();
//    }


    @Operation(summary = "특정 리뷰 삭제", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "특정 리뷰 삭제 성공"),
        @ApiResponse(responseCode = "404",description = "아이템 아이디 혹은 리뷰 아이디가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "403",description = "접근 권한이 없습니다.",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/items/{itemId}/ratings/{ratingId}")
    @LogExecutionTime
    public ResponseEntity<ResponsePageDTO> remove(
        @Parameter(description = "리뷰 받을 아이템 ID", required = true) @PathVariable Long itemId,
        @Parameter(description = "리뷰 ID", required = true) @PathVariable Long ratingId,
        @Parameter(hidden = true) @AuthMember Member member) {
        ratingService.remove(itemId,ratingId, member.getId());
        return ResponseEntity.ok().build();
    }

}
