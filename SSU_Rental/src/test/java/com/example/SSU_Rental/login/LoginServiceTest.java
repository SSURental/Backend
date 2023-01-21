package com.example.SSU_Rental.login;


import com.example.SSU_Rental.exception.notfound.MemberNotFound;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import com.example.SSU_Rental.member.MemberRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoginServiceTest {


    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LoginService loginService;


    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    public void clear(){
        memberRepository.deleteAll();
        sessionRepository.deleteAll();
    }


    @Test
    @DisplayName("로그인 성공하면 세션 토큰이 발급됩니다.")
    public void test1(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);

        //Act
        String accessToken = loginService.login(new LoginDTO("user1", "password1"));

        //Assert
        Session session = sessionRepository.findByAccessToken(accessToken).get();
        Assertions.assertEquals(session.getMember().getId(),member.getId());
    }

    @Test
    @DisplayName("비밀번호 혹은 로그인 아이디를 잘못 입력하면 로그인이 실패합니다.")
    public void test2(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);

        //Act
        Assertions.assertThrows(MemberNotFound.class,()->{loginService.login(new LoginDTO("user2", "password1"));});

    }


    private Member createMember(String loginId, String password, String name, String group,
        String imgName) {
        return Member.createMember(
            MemberRequest.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .group(group)
                .imageDTO(new ImageDTO(imgName))
                .build());
    }

}