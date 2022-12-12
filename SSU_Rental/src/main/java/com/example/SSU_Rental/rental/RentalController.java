package com.example.SSU_Rental.rental;

import com.example.SSU_Rental.board.BoardResponse;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
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
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
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


    @Operation(summary = "렌탈 하나 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "아이템 한개 렌탈 성공", content = @Content(schema = @Schema(implementation = Long.class))),
        @ApiResponse(responseCode = "404",description = "아이템 아이디가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/items/{itemId}/rentals")
    public ResponseEntity<Long> rental(
        @Parameter(description = "렌탈할 아이템 ID", required = true) @PathVariable Long itemId,
        @Parameter(hidden = true) @AuthMember Member member) {
        Long rentalId = rentalService.rental(itemId, member.getId());
        return ResponseEntity.created(URI.create("/item/" + itemId + "/" + rentalId))
            .body(rentalId);
    }


    @Operation(summary = "렌탈 정보 하나 가져오기(GetOne)", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "렌탈 정보 하나 가져오기(GetOne) 성공", content = @Content(schema = @Schema(implementation = RentalResponse.class))),
        @ApiResponse(responseCode = "404",description = "아이템 아이디 혹은 렌탈 아이디가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/items/{itemId}/rentals/{rentalId}")
    public ResponseEntity<RentalResponse> getOne(
        @Parameter(description = "아이템 ID", required = true) @PathVariable Long itemId,
        @Parameter(description = "렌탈 ID", required = true) @PathVariable Long rentalId) {
        return ResponseEntity.ok().body(rentalService.getOne(itemId, rentalId));
    }

    /**
     * 내가 대여한 리스트 목록 가져오기( 타인의 대여 리스트는 굳이 알 필요X)
     */
    @Operation(summary = "나의 렌탈 목록  요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "내가 렌탈한 아이템 목록 가져오기 성공", content = @Content(schema = @Schema(implementation = ResponsePageDTO.class))),
        @ApiResponse(responseCode = "404",description = "멤버가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/rentals")
    public ResponseEntity<ResponsePageDTO> getMyRentalList(
        @Parameter(hidden = true) @AuthMember Member member,
        @ParameterObject RequestPageDTO requestPageDTO) {
        return ResponseEntity.ok()
            .body(rentalService.getMyRentalList(member.getId(), requestPageDTO));
    }

    /**
     * 대여 기한 연장
     */

    @Operation(summary = "렌탈기간  일주일 연장  요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "렌탈 기간 일주일 연장 성공", content = @Content(schema = @Schema(implementation = RentalResponse.class))),
        @ApiResponse(responseCode = "404",description = "아이템 아이디 혹은 렌탈 아이디가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "403",description = "접근 권한이 없습니다.",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PatchMapping("/items/{itemId}/rentals/{rentalId}")
    public ResponseEntity<RentalResponse> extendRental(
        @Parameter(description = "아이템 ID", required = true) @PathVariable Long itemId,
        @Parameter(description = "렌탈 ID", required = true) @PathVariable Long rentalId,
        @Parameter(hidden = true) @AuthMember Member member) {
        return ResponseEntity.ok()
            .body(rentalService.extendRental(itemId, rentalId, member.getId()));
    }

    @Operation(summary = "렌탈한 아이템 반납 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "렌탈 아이템 반납 성공"),
        @ApiResponse(responseCode = "404",description = "아이템 아이디 혹은 렌탈 아이디가 잘못되었습니다",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "403",description = "접근 권한이 없습니다.",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/items/{itemId}/rentals/{rentalId}")
    public ResponseEntity returnItem(
        @Parameter(description = "아이템 ID", required = true) @PathVariable Long itemId,
        @Parameter(description = "렌탈 ID", required = true) @PathVariable Long rentalId,
        @Parameter(hidden = true) @AuthMember Member member) {
        rentalService.returnItem(itemId, rentalId, member.getId());
        return ResponseEntity.ok().build();
    }
}
