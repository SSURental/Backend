package com.example.SSU_Rental.login;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import com.example.SSU_Rental.member.MemberRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    public void clear(){
        sessionRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    public void test1()throws Exception{
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        LoginDTO loginDTO = new LoginDTO("user1","password1");
        String json = objectMapper.writeValueAsString(loginDTO);

        //Act
        mockMvc.perform(post("/login").contentType(APPLICATION_JSON).content(json))
            .andDo(print());

    }

    @Test
    @DisplayName("로그인 실패 -> 아이디 및 비밀번호는 공백이 될 수 없습니다.")
    public void test2()throws Exception{
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        LoginDTO loginDTO = new LoginDTO("","");
        String json = objectMapper.writeValueAsString(loginDTO);

        //Act
        mockMvc.perform(post("/login").contentType(APPLICATION_JSON).content(json))
            //Assert
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", is("400")))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validation.loginId").value("아이디는 공백이 될 수 없습니다. "))
            .andExpect(jsonPath("$.validation.password").value("비밀번호는 공백이 될 수 없습니다."))
            .andDo(print());

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