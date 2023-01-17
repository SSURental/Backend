package com.example.SSU_Rental.login;

import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {


    private final MemberRepository memberRepository;


    @Transactional
    public String login(LoginDTO loginDTO){

        Member member = memberRepository.findByLoginIdAndPassword(loginDTO.getLoginId(),
                loginDTO.getPassword())
            .orElseThrow(() -> new CustomException(ErrorMessage.INVALID_LOGIN_REQUEST_ERROR));

        String accessToken = member.addSession();
        return accessToken;
    }

}
