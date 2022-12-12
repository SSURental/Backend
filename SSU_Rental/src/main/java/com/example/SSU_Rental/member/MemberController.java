package com.example.SSU_Rental.member;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.ErrorResponseDTO;
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
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "회원", description = "회원 관련 API")
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 가입 요청")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PostMapping("/members")
    public ResponseEntity<Long> register(@Validated @RequestBody MemberRequest memberRequest) { // 회원 추가

        Long registerId = memberService.register(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + registerId)).body(registerId);
    }

    @Operation(summary = "회원 1명 정보 요청", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 1명 정보 가져오기 성공", content = @Content(schema = @Schema(implementation = MemberResponse.class))),
        @ApiResponse(responseCode = "404", description = "멤버 아이디가 잘못되었습니다", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/members/{memberId}")
    private ResponseEntity<MemberResponse> getOne(
        @Parameter(description = "회원 ID", required = true) @PathVariable Long memberId) {
        MemberResponse response = memberService.getOne(memberId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 멤버 정보 수정 -> 이름, 속한 그룹, 이미지만 변경 가능
     */
    @Operation(summary = "회원 1명 정보 수정(자신의 정보만 수정 가능)", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 1명 정보 수정 성공", content = @Content(schema = @Schema(implementation = Long.class))),
        @ApiResponse(responseCode = "404", description = "멤버 아이디가 잘못되었습니다", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PatchMapping("/members/{memberId}")
    private ResponseEntity<Long> modify(
        @Parameter(description = "회원 ID", required = true) @PathVariable Long memberId,
        @RequestBody MemberEdit memberEdit, @Parameter(hidden = true) @AuthMember Member member) {
        memberService.modify(memberId, memberEdit, member.getId());
        return ResponseEntity.ok().body(memberId);
    }

    @Operation(summary = "나의 아이템 목록 가져오기", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "나의 아이템 목록 가져오기 성공", content = @Content(schema = @Schema(implementation = ResponsePageDTO.class))),
        @ApiResponse(responseCode = "404", description = "멤버 아이디가 잘못되었습니다", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    @GetMapping("/members/items")
    private ResponseEntity<ResponsePageDTO> getMyItem(
        @ParameterObject RequestPageDTO requestPageDTO,
        @Parameter(hidden = true) @AuthMember Member member) {
        ResponsePageDTO responsePage = memberService.getMyItemList(requestPageDTO, member.getId());

        return ResponseEntity.ok().body(responsePage);
    }


    /**
     * 내가(로그인한 객체) 쓴 댓글 모음
     *
     * @param requestPageDTO
     * @param member
     * @return
     */

    @Operation(summary = "나의 댓글 목록 가져오기", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "나의 댓글 목록 가져오기 성공", content = @Content(schema = @Schema(implementation = ResponsePageDTO.class))),
        @ApiResponse(responseCode = "404", description = "멤버 아이디가 잘못되었습니다", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    @GetMapping("/members/replys")
    private ResponseEntity<ResponsePageDTO> getMyReply(
        @ParameterObject RequestPageDTO requestPageDTO,
        @Parameter(hidden = true) @AuthMember Member member) {
        ResponsePageDTO responsePage = memberService.getMyReplyList(requestPageDTO, member.getId());

        return ResponseEntity.ok().body(responsePage);
    }


    /**
     * 내가(로그인한 객체) 쓴 게시글 모음
     *
     * @param requestPageDTO
     * @param member
     * @return
     */

    @Operation(summary = "나의 게시글 목록 가져오기", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "나의 게시글 목록 가져오기 성공", content = @Content(schema = @Schema(implementation = ResponsePageDTO.class))),
        @ApiResponse(responseCode = "404", description = "멤버 아이디가 잘못되었습니다", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    @GetMapping("/members/boards")
    private ResponseEntity<ResponsePageDTO> getMyBoard(
        @ParameterObject RequestPageDTO requestPageDTO,
        @Parameter(hidden = true) @AuthMember Member member) {
        ResponsePageDTO responsePage = memberService.getMyBoardList(requestPageDTO, member.getId());

        return ResponseEntity.ok().body(responsePage);
    }

    @Operation(summary = "나의 평가 목록 가져오기", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "내가 평가한 목록 가져오기 성공", content = @Content(schema = @Schema(implementation = ResponsePageDTO.class))),
        @ApiResponse(responseCode = "404", description = "멤버 아이디가 잘못되었습니다", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    @GetMapping("/members/ratings")
    private ResponseEntity<ResponsePageDTO> getMyRating(
        @ParameterObject RequestPageDTO requestPageDTO,
        @Parameter(hidden = true) @AuthMember Member member) {
        ResponsePageDTO responsePage = memberService.getMyRatingList(requestPageDTO, member.getId());
        return ResponseEntity.ok().body(responsePage);
    }


    @Operation(summary = "나의 렌탈 목록 가져오기", parameters = {
        @Parameter(in = ParameterIn.HEADER, name = "X-AUTH-TOKEN", required = true, description = "발급받은 JWT토큰")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "나의 렌탈 목록 가져오기 성공", content = @Content(schema = @Schema(implementation = ResponsePageDTO.class))),
        @ApiResponse(responseCode = "404", description = "멤버 아이디가 잘못되었습니다", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    @GetMapping("/members/rentals")
    private ResponseEntity<ResponsePageDTO> getMyRental(
        @ParameterObject RequestPageDTO requestPageDTO,
        @Parameter(hidden = true) @AuthMember Member member) {
        ResponsePageDTO responsePage = memberService.getMyRentalList(requestPageDTO, member.getId());
        return ResponseEntity.ok().body(responsePage);
    }

}