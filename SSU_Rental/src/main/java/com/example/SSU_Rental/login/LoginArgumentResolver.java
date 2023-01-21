package com.example.SSU_Rental.login;

import com.example.SSU_Rental.config.AppConfig;
import com.example.SSU_Rental.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;
    private final AppConfig appConfig;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
           return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest servletRequest =webRequest.getNativeRequest(HttpServletRequest.class);
//        if(servletRequest==null){
//            log.error("HttpServletRequest null");
//            throw new UnauthorizedException();
//        }
//        Cookie[] cookies = servletRequest.getCookies();
//        if(cookies.length==0){
//            log.error("쿠키가 존재하지 않습니다.");
//            throw new UnauthorizedException(); // 쿠키X -> 로그인 경험 X -> 비인증된 사용자
//        }
//
//        String accessToken = cookies[0].getValue();
//        Session session = sessionRepository.findByAccessToken(accessToken)
//            .orElseThrow(() -> new UnauthorizedException()); // 세션이 잘못됨 -> 비인증된 사용자
//
//        UserSession userSession = new UserSession(session.getMember().getId());
//        return userSession;

        String jws = webRequest.getHeader("Authorization");
        if(jws==null||jws.equals("")){
            throw new UnauthorizedException();
        }

        byte[] decodedKey = Base64.decodeBase64(appConfig.getKey());

        try {

            Jws<Claims> claims = Jwts.parserBuilder().
                setSigningKey(decodedKey)
                .build()
                .parseClaimsJws(jws);

            //OK, we can trust this JWT
            String memberId = claims.getBody().getSubject();
            return new UserSession(Long.parseLong(memberId));
        } catch (JwtException e) {

        //don't trust the JWT!
            throw new UnauthorizedException();
    }

    }
}
