package com.example.SSU_Rental.login;

import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.exception.ErrorMessage;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {


    private final MemberRepository memberRepository;


    public String login(LoginDTO loginDTO){

        Member member = memberRepository.findByLoginIdAndPassword(loginDTO.getLoginId(),
                loginDTO.getPassword())
            .orElseThrow(() -> new CustomException(ErrorMessage.INVALID_LOGIN_REQUEST_ERROR));

        String accessToken = member.addSession();
        return accessToken;
    }

}
