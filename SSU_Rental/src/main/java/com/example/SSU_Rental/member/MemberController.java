package com.example.SSU_Rental.member;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "회원",description = "회원 관련 API")
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 가입 요청")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PostMapping("/members")
    public ResponseEntity<Long> register(@RequestBody MemberRequest memberRequest) { // 회원 추가

        Long registerId = memberService.register(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + registerId)).body(registerId);
    }

    @Operation(summary = "회원 1명 정보 요청")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "회원 1명 정보 가져오기 성공", content = @Content(schema = @Schema(implementation = MemberResponse.class)))
    })
    @GetMapping("/members/{memberId}")
    private ResponseEntity<MemberResponse> getOne(@Parameter(description = "회원 ID",required = true) @PathVariable Long memberId){
        MemberResponse response = memberService.getOne(memberId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 멤버 정보 수정 -> 이름, 속한 그룹, 이미지만 변경 가능
     */

    @PatchMapping("/members/{memberId}")
    private ResponseEntity<Long> modify(@Parameter(description = "회원 ID",required = true) @PathVariable Long memberId,@RequestBody MemberEdit memberEdit ,@Parameter(hidden = true) @AuthMember Member member){
        memberService.modify(memberId,memberEdit,member.getId());
        return ResponseEntity.ok().body(memberId);
    }

    @GetMapping("/members/items")
    private ResponseEntity<ResponsePageDTO> getMyItem(@ParameterObject RequestPageDTO requestPageDTO,@AuthMember Member member){
        ResponsePageDTO responsePage = memberService.getMyItem(requestPageDTO, member.getId());

        return ResponseEntity.ok().body(responsePage);
    }


    /**
     * 내가(로그인한 객체) 쓴 댓글 모음
     * @param requestPageDTO
     * @param member
     * @return
     */

    @GetMapping("/members/replys")
    private ResponseEntity<ResponsePageDTO> getMyReply(@ParameterObject RequestPageDTO requestPageDTO,@AuthMember Member member){
        ResponsePageDTO responsePage = memberService.getMyReply(requestPageDTO, member.getId());

        return ResponseEntity.ok().body(responsePage);
    }



    /**
     * 내가(로그인한 객체) 쓴 게시글 모음
     * @param requestPageDTO
     * @param member
     * @return
     */

    @GetMapping("/members/boards")
    private ResponseEntity<ResponsePageDTO> getMyBoard(@ParameterObject RequestPageDTO requestPageDTO,@AuthMember Member member){
        ResponsePageDTO responsePage = memberService.getMyBoard(requestPageDTO, member.getId());

        return ResponseEntity.ok().body(responsePage);
    }

    @GetMapping("/members/ratings")
    private ResponseEntity<ResponsePageDTO> getMyRating(@ParameterObject RequestPageDTO requestPageDTO,@AuthMember Member member){
        ResponsePageDTO responsePage = memberService.getMyRating(requestPageDTO,member.getId());
        return ResponseEntity.ok().body(responsePage);
    }




}