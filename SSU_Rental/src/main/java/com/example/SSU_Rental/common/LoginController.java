package com.example.SSU_Rental.common;

import com.example.SSU_Rental.config.JwtTokenProvider;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인",description = "로그인 관련 API")
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Operation(summary = "로그인 요청")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "로그인 성공-> 토큰 반환", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO){

        Member member = memberRepository.findByLoginId(loginDTO.getLoginId())
            .orElseThrow(() -> new IllegalArgumentException("없는 아이디입니다."));

        if(!passwordEncoder.matches(loginDTO.getPassword(),member.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호");
        }

        return jwtTokenProvider.createToken(member.getLoginId(),member.getMemberGroup().name());

    }
}
