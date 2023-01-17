package com.example.SSU_Rental.board;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.exception.ErrorMessage;
import com.example.SSU_Rental.login.LoginDTO;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
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
@TestPropertySource(locations = "/application.yml")
class BoardControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Cookie cookie;

    private Member member;

    @BeforeEach
    void ready() throws Exception {
        member = Member.builder()
            .loginId("user1")
            .password("password1")
            .memberGroup(Group.STUDENT)
            .name("유저")
            .build();

        memberRepository.save(member);

        LoginDTO loginDTO = new LoginDTO(member.getLoginId(), member.getPassword());
        String json = objectMapper.writeValueAsString(loginDTO);

        cookie = mockMvc.perform(
                post("/login").contentType(APPLICATION_JSON).content(json))
            .andDo(print())
            .andReturn().getResponse().getCookie("SESSION");

    }

    @AfterEach
    void clean() {
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("게시글 작성 테스트")
    public void test1() throws Exception {
        BoardRequest boardRequest = new BoardRequest("제목", "내용");
        String json = objectMapper.writeValueAsString(boardRequest);
        mockMvc.perform(post("/boards").contentType(APPLICATION_JSON).content(json).cookie(cookie))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성 테스트2-제목 혹은 내용은 공백이 될수 없습니다.")
    public void test2() throws Exception {
        BoardRequest boardRequest = new BoardRequest("", "내용");
        String json = objectMapper.writeValueAsString(boardRequest);
        mockMvc.perform(post("/boards").contentType(APPLICATION_JSON).content(json).cookie(cookie))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", is(400)))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("제목은 공백이 될 수 없습니다."))
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 하나 조회")
    public void test3() throws Exception {

        Board board = Board.createBoard(new BoardRequest("제목", "내용"), member);
        boardRepository.save(board);

        mockMvc.perform(get("/boards/{boardId}", board.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("제목"))
            .andExpect(jsonPath("$.content").value("내용"))
            .andExpect(jsonPath("$.nickname").value("유저"))
            .andExpect(jsonPath("$.views").value(1))
            .andExpect(jsonPath("$.likes").value(0))
            .andExpect(jsonPath("$.dislikes").value(0))
            .andExpect(jsonPath("$.warns").value(0))
            .andDo(print());

    }

    @Test
    @DisplayName("게시글 하나 조회 실패")
    public void test4() throws Exception {

        Board board = Board.createBoard(new BoardRequest("제목", "내용"), member);
        boardRepository.save(board);

        mockMvc.perform(get("/boards/{boardId}", 10L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code",is(404)))
            .andExpect(jsonPath("$.status").value("NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("해당 게시글은 존재하지 않습니다."))
            .andDo(print());

    }


    @Test
    @DisplayName("페이징 테스트")
    public void test5() throws Exception {

        List<Board> boardList = IntStream.rangeClosed(1, 20)
            .mapToObj(i -> Board.createBoard(new BoardRequest("제목" + i, "내용" + i), member))
            .collect(Collectors.toList());

        boardRepository.saveAll(boardList);

        mockMvc.perform(get("/boards?page=1&size=5").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", is(1)))
            .andExpect(jsonPath("$.size", is(5)))
            .andExpect(jsonPath("$.totalPage", is(4)))
            .andExpect(jsonPath("$.hasNext", is(true)))
            .andExpect(jsonPath("$.contents[0].title").value("제목20"))
            .andExpect(jsonPath("$.contents[0].content").value("내용20"))
            .andExpect(jsonPath("$.contents[0].nickname").value("유저"))
            .andExpect(jsonPath("$.contents[0].views").value(0))
            .andExpect(jsonPath("$.contents[0].likes").value(0))
            .andExpect(jsonPath("$.contents[0].dislikes").value(0))
            .andExpect(jsonPath("$.contents[0].warns").value(0))
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정")
    public void test6() throws Exception {
        Board board = Board.createBoard(new BoardRequest("제목", "내용"), member);
        boardRepository.save(board);

        BoardEdit boardEdit = new BoardEdit("제목-수정", "");
        String json = objectMapper.writeValueAsString(boardEdit);

        mockMvc.perform(
                patch("/boards/{boardId}", board.getId()).contentType(APPLICATION_JSON).content(json)
                    .cookie(cookie))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(board.getId()))
            .andDo(print());

        Board editBoard = boardRepository.findById(board.getId())
            .orElseThrow(() -> new CustomException(ErrorMessage.BOARD_NOT_FOUND_ERROR));

        Assertions.assertThat(editBoard.getTitle()).isEqualTo("제목-수정");
        Assertions.assertThat(editBoard.getContent()).isEqualTo("내용");


    }

    @Test
    @DisplayName("게시글 수정 권한이 없습니다.")
    public void test7() throws Exception {
        Member member2 = Member.builder()
            .loginId("user2")
            .password("password2")
            .memberGroup(Group.SCHOOL)
            .name("유저2")
            .build();

        memberRepository.save(member2);
        Board board = Board.createBoard(new BoardRequest("제목", "내용"), member2);
        boardRepository.save(board);

        BoardEdit boardEdit = new BoardEdit("제목-수정", "");
        String json = objectMapper.writeValueAsString(boardEdit);

        mockMvc.perform(
                patch("/boards/{boardId}", board.getId()).contentType(APPLICATION_JSON).content(json)
                    .cookie(cookie))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.code",is(403)))
            .andExpect(jsonPath("$.status").value("FORBIDDEN"))
            .andExpect(jsonPath("$.message").value("해당 컨텐츠에 접근할 권한이 없습니다"))
            .andDo(print());



    }


    @Test
    @DisplayName("게시글 삭제")
    public void test8() throws Exception {
        Board board = Board.createBoard(new BoardRequest("제목", "내용"), member);
        boardRepository.save(board);

        mockMvc.perform(delete("/boards/{boardId}", board.getId()).cookie(cookie))
            .andExpect(status().isOk())
            .andDo(print());

        Assertions.assertThatThrownBy(()->boardRepository.findById(board.getId()).orElseThrow(()->new CustomException(ErrorMessage.BOARD_NOT_FOUND_ERROR)))
            .isInstanceOf(CustomException.class);
    }


    @Test
    @DisplayName("게시글 삭제 권한이 없습니다.")
    public void test9() throws Exception {
        Member member2 = Member.builder()
            .loginId("user2")
            .password("password2")
            .memberGroup(Group.SCHOOL)
            .name("유저2")
            .build();

        memberRepository.save(member2);
        Board board = Board.createBoard(new BoardRequest("제목", "내용"), member2);
        boardRepository.save(board);


        mockMvc.perform(
                delete("/boards/{boardId}", board.getId()).cookie(cookie))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.code",is(403)))
            .andExpect(jsonPath("$.status").value("FORBIDDEN"))
            .andExpect(jsonPath("$.message").value("해당 컨텐츠에 접근할 권한이 없습니다"))
            .andDo(print());



    }

    @Test
    @DisplayName("게시글  조회 실패")
    public void test10() throws Exception {

        Board board = Board.createBoard(new BoardRequest("제목", "내용"), member);
        boardRepository.save(board);

        mockMvc.perform(delete("/boards/{boardId}", 100L).cookie(cookie))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code",is(404)))
            .andExpect(jsonPath("$.status").value("NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("해당 게시글은 존재하지 않습니다."))
            .andDo(print());

    }



}