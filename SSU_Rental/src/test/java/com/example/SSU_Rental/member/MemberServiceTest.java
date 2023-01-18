package com.example.SSU_Rental.member;

import static org.junit.jupiter.api.Assertions.*;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.exception.ForbiddenException;
import com.example.SSU_Rental.exception.notfound.MemberNotFound;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.login.UserSession;
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
        Member member = memberRepository.getMember(memberId);
        Assertions.assertEquals(member.getLoginId(),"user1");
        Assertions.assertEquals(member.getPassword(),"password1");
        Assertions.assertEquals(member.getName(),"유저1");
        Assertions.assertEquals(member.getMemberGroup(), Group.STUDENT);
        Assertions.assertEquals(member.getMemberImage().getImgName(),"member-img01");
//         LazyInitializationException 예외로 인해 멤버이미지 이름을 확인하기 불가능 -> 방법을 찾아야..
//      특이점) 만약 멤버 이미지의 아이디는 조회 가능(아마도 멤버이미지 아이디는 멤버에 들어있기 때문인듯)

    }


    @Test
    @DisplayName("회원 1명 조회")
    public void test2(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);

        //Act
        MemberResponse response = memberService.getOne(member.getId());

        //Arrange
        assertEquals(response.getId(),member.getId());
        assertEquals(response.getLoginId(),"user1");
        assertEquals(response.getGroup(),"STUDENT");
        assertEquals(response.getName(),"유저1");
        assertEquals(response.getImageDTO().getImgName(),"member-img01");

    }

    /**
     * Test3은 테스트 가치가 있는지 잘 모르겠다.(너무 로직이 단순함)
     */
//    @Test
//    @DisplayName("잘못된 회원 아이디(혹은 삭제된 회원 아이디)로 멤버를 조회할 수 없습니다.")
//    public void test3(){
//        //Arrange
//        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
//        memberRepository.save(member);
//
//        //Act
//        assertThrows(NullPointerException.class,()->{memberService.getOne(member.getId()+2L);});
//
//    }


    @Test
    @DisplayName("회원 수정")
    public void test4(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);

        //Act
        memberService.edit(member.getId(),new MemberEdit("유저2",new ImageDTO("")),createUserSession(member));

        //Assert
        Member findMember = memberRepository.getMember(member.getId());
        assertEquals(findMember.getName(),"유저2");
        assertEquals(findMember.getMemberImage().getImgName(),"member-img01");

    }

    @Test
    @DisplayName("회원 수정 실패(이유: 나의 회원 수정이 아닌 다른 사람의 회원 정보를 수정하려함)")
    public void test5(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-img02");
        memberRepository.save(anotherMember);

        //Act
        assertThrows(ForbiddenException.class,()->{memberService.edit(member.getId(),new MemberEdit("유저2",new ImageDTO("")),createUserSession(anotherMember));});
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

    private UserSession createUserSession(Member member){
        return new UserSession(member.getId());
    }
}