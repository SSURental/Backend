package com.example.SSU_Rental.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberDetailsService memberService;

    @PostMapping("/user")
    public String signup(MemberRequest memberRequest) { // 회원 추가

        return "redirect:/login";
    }
}