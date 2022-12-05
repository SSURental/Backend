package com.example.SSU_Rental.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
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



}