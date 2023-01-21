package com.example.SSU_Rental.boardrp;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardRepository;
import com.example.SSU_Rental.board.BoardRequest;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.login.LoginDTO;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import com.example.SSU_Rental.member.MemberRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BoardrpControllerTest {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardrpRepository boardrpRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;


    @AfterEach
    public void clean() {
        boardrpRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 작성")
    public void test1() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Cookie cookie = createCookie(member);
        Board board = createBoard("게시글-제목", "게시글-내용", member);
        boardRepository.save(board);
        BoardrpRequest boardrpRequest = new BoardrpRequest("댓글");
        String json = objectMapper.writeValueAsString(boardrpRequest);

        //Act
        mockMvc.perform(
                post("/boards/{boardId}/replys", board.getId()).contentType(APPLICATION_JSON)
                    .content(json).cookie(cookie))
            //Assert
            .andExpect(status().isCreated())
            .andDo(print());

    }

    @Test
    @DisplayName("댓글 작성 실패 -> 이유: 내용은 공백이 될 수 없다.")
    public void test2() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Cookie cookie = createCookie(member);
        Board board = createBoard("게시글-제목", "게시글-내용", member);
        boardRepository.save(board);
        BoardrpRequest boardrpRequest = new BoardrpRequest("");
        String json = objectMapper.writeValueAsString(boardrpRequest);

        //Act
        mockMvc.perform(
                post("/boards/{boardId}/replys", board.getId()).contentType(APPLICATION_JSON)
                    .content(json).cookie(cookie))
            //Assert
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", is("400")))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validation.content").value("내용은 공백이 될 수 없습니다."))
            .andDo(print());

    }

    @Test
    @DisplayName("댓글 페이지 테스트")
    public void test3() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저", "STUDENT", "img01");
        memberRepository.save(member);
        Board board = createBoard("게시글-제목", "게시글-내용", member);
        boardRepository.save(board);
        List<Boardrp> boardrpList = IntStream.rangeClosed(1, 21)
            .mapToObj(i -> Boardrp.createBoardrp(board, member, new BoardrpRequest("내용" + i)))
            .collect(Collectors.toList());
        boardrpRepository.saveAll(boardrpList);

        //Act
        mockMvc.perform(get("/boards/{boardId}/replys?page=1&size=5",board.getId()).contentType(APPLICATION_JSON))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", is(1)))
            .andExpect(jsonPath("$.size", is(5)))
            .andExpect(jsonPath("$.totalPage", is(5)))
            .andExpect(jsonPath("$.hasNext", is(true)))
            .andExpect(jsonPath("$.contents[0].content").value("내용21"))
            .andExpect(jsonPath("$.contents[0].nickname").value("유저"))
            .andExpect(jsonPath("$.contents[0].boardId").value(board.getId()))
            .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정")
    public void test4() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저", "STUDENT", "img01");
        memberRepository.save(member);
        Cookie cookie = createCookie(member);
        Board board = createBoard("게시글-제목", "게시글-내용", member);
        boardRepository.save(board);
        Boardrp boardrp = createBoardrp(board, member, "내용");
        boardrpRepository.save(boardrp);
        BoardrpEdit boardrpEdit = new BoardrpEdit("내용-수정");
        String json = objectMapper.writeValueAsString(boardrpEdit);

        //Act
        mockMvc.perform(
                patch("/boards/{boardId}/replys/{replyId}", board.getId(),boardrp.getId()).contentType(APPLICATION_JSON)
                    .content(json).cookie(cookie))
            //Assert
            .andExpect(status().isOk())
            .andDo(print());
    }


    @Test
    @DisplayName("댓글 삭제")
    public void test5() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저", "STUDENT", "img01");
        memberRepository.save(member);
        Cookie cookie = createCookie(member);
        Board board = createBoard("게시글-제목", "게시글-내용", member);
        boardRepository.save(board);
        Boardrp boardrp = createBoardrp(board, member, "내용");
        boardrpRepository.save(boardrp);


        //Act
        mockMvc.perform(
                delete("/boards/{boardId}/replys/{replyId}", board.getId(),boardrp.getId()).cookie(cookie))
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

    private Boardrp createBoardrp(Board board,Member member,String content){
        return Boardrp.createBoardrp(board, member, new BoardrpRequest(content));
    }
}