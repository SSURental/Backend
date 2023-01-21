package com.example.SSU_Rental.login;

import com.example.SSU_Rental.config.AppConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private static final String KEY = "";
    private final LoginService loginService;
    private final AppConfig appConfig;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO) {

//        String accessToken = loginService.login(loginDTO);
//        ResponseCookie responseCookie = ResponseCookie.from("SESSION", accessToken)
//            .httpOnly(true)
//            .domain("localhost") // todo 서버에 따른 분리 필요(Domain: 어떤 도메인에서 쿠키를 가져갈 수 있는지) localhost를 대상으로 한 요청에만  쿠키가 전송됩니다.
//            // 별도의 값이 없으면 쿠키를 보낸 도메인의 서버로 설정
//            .path("/") // 특정 경로의  하위 경로에서만 쿠키를 포함하여 전송
//            .secure(false)
//            .maxAge(Duration.ofDays(30))
//            .sameSite("Strict")
//            .build();

        Long memberId = loginService.login(loginDTO);
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(appConfig.getKey()));
        String jws = Jwts.builder()
            .setSubject(String.valueOf(memberId))
            .signWith(key)
            .compact();

//        return ResponseEntity.ok().header("SET-COOKIE", responseCookie.toString()).build();

        return ResponseEntity.ok().body(jws);


    }
}
