package com.example.SSU_Rental.member;

import static org.junit.jupiter.api.Assertions.*;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.image.ImageDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    public void clear(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입")
    public void test1(){
        //Arrange
        MemberRequest memberRequest = MemberRequest.builder().loginId("user1").password("password1")
            .name("유저1")
            .group("STUDENT").imageDTO(new ImageDTO("member-img01")).build();

        //Act
        Long memberId = memberService.register(memberRequest);

        //Arrange
        Member member = memberRepository.findAll().get(0);
        Assertions.assertEquals(member.getLoginId(),"user1");
        Assertions.assertEquals(member.getPassword(),"password1");
        Assertions.assertEquals(member.getName(),"유저1");
        Assertions.assertEquals(member.getMemberGroup(), Group.STUDENT);
        Assertions.assertEquals(member.getMemberImage().getImgName(),"member-img01");

    }

}