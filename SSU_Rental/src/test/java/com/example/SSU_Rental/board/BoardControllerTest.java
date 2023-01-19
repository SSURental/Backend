package com.example.SSU_Rental.board;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.login.LoginDTO;
import com.example.SSU_Rental.login.UserSession;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import com.example.SSU_Rental.member.MemberRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;



    @BeforeEach
    void clear() {
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("게시글 작성 테스트")
    public void test1() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Cookie cookie = createCookie(member);
        BoardRequest boardRequest = new BoardRequest("제목", "내용");
        String json = objectMapper.writeValueAsString(boardRequest);
        //Act
        mockMvc.perform(post("/boards").contentType(APPLICATION_JSON).content(json).cookie(cookie))
            //Assert
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성 실패->제목 혹은 내용은 공백이 될수 없습니다.")
    public void test2() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Cookie cookie = createCookie(member);
        BoardRequest boardRequest = new BoardRequest("", "");
        String json = objectMapper.writeValueAsString(boardRequest);
        //Act
        mockMvc.perform(post("/boards").contentType(APPLICATION_JSON).content(json).cookie(cookie))
            //Assert
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", is("400")))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validation.content").value("내용은 공백이 될 수 없습니다."))
            .andExpect(jsonPath("$.validation.title").value("제목은 공백이 될 수 없습니다."))
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 하나 조회")
    public void test3() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);

        //Act
        mockMvc.perform(get("/boards/{boardId}", board.getId()))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(board.getId()))
            .andExpect(jsonPath("$.title").value("제목"))
            .andExpect(jsonPath("$.content").value("내용"))
            .andExpect(jsonPath("$.nickname").value("유저1"))
            .andExpect(jsonPath("$.views").value(1))
            .andExpect(jsonPath("$.likes").value(0))
            .andExpect(jsonPath("$.dislikes").value(0))
            .andExpect(jsonPath("$.warns").value(0))
            .andDo(print());

    }



    @Test
    @DisplayName("페이징 테스트")
    public void test5() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        List<Board> boardList = IntStream.rangeClosed(1, 20)
            .mapToObj(i ->createBoard("제목"+i,"내용"+i,member))
            .collect(Collectors.toList());
        boardRepository.saveAll(boardList);

        //Act
        mockMvc.perform(get("/boards?page=1&size=5").contentType(APPLICATION_JSON))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", is(1)))
            .andExpect(jsonPath("$.size", is(5)))
            .andExpect(jsonPath("$.totalPage", is(4)))
            .andExpect(jsonPath("$.hasNext", is(true)))
            .andExpect(jsonPath("$.contents[0].title").value("제목20"))
            .andExpect(jsonPath("$.contents[0].content").value("내용20"))
            .andExpect(jsonPath("$.contents[0].nickname").value("유저1"))
            .andExpect(jsonPath("$.contents[0].views").value(0))
            .andExpect(jsonPath("$.contents[0].likes").value(0))
            .andExpect(jsonPath("$.contents[0].dislikes").value(0))
            .andExpect(jsonPath("$.contents[0].warns").value(0))
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정")
    public void test6() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Cookie cookie = createCookie(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        BoardEdit boardEdit = new BoardEdit("제목-수정", "");
        String json = objectMapper.writeValueAsString(boardEdit);

        //Act
        mockMvc.perform(patch("/boards/{boardId}",board.getId()).contentType(APPLICATION_JSON).content(json)
                    .cookie(cookie))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(board.getId()))
            .andDo(print());
    }


    @Test
    @DisplayName("게시글 삭제")
    public void test7() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Cookie cookie = createCookie(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        BoardEdit boardEdit = new BoardEdit("제목-수정", "");
        String json = objectMapper.writeValueAsString(boardEdit);

        //Act
        mockMvc.perform(delete("/boards/{boardId}",board.getId()).contentType(APPLICATION_JSON).content(json)
                .cookie(cookie))
            //Assert
            .andExpect(status().isOk())
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

    private Cookie createCookie(Member member) throws Exception {
        LoginDTO loginDTO = new LoginDTO(member.getLoginId(), member.getPassword());
        String json = objectMapper.writeValueAsString(loginDTO);

        return mockMvc.perform(
                post("/login").contentType(APPLICATION_JSON).content(json))
            .andDo(print())
            .andReturn().getResponse().getCookie("SESSION");
    }

    private Board createBoard(String title, String content, Member member) {
        return Board.createBoard(new BoardRequest(title, content), member);
    }


}