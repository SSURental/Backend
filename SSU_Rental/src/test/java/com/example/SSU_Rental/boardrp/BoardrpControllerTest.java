package com.example.SSU_Rental.boardrp;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardRepository;
import com.example.SSU_Rental.board.BoardRequest;
import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest
@AutoConfigureMockMvc
class BoardrpControllerTest {


    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardrpRepository boardrpRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;



    @AfterEach
    public void clean(){
        boardRepository.deleteAll();
        boardrpRepository.deleteAll();
        memberRepository.deleteAll();
    }



    @Test
    @DisplayName("댓글 페이지 테스트")
    public void test1() throws Exception {

        Member member = Member.builder()
            .loginId("user1")
            .password("password1")
            .memberGroup(Group.STUDENT)
            .name("유저")
            .build();

        memberRepository.save(member);

        Board board = Board.createBoard(new BoardRequest("제목", "내용"), member);
        boardRepository.save(board);

        List<Boardrp> boardrpList = IntStream.rangeClosed(1, 21)
            .mapToObj(i -> Boardrp.createBoardrp(board, member, new BoardrpRequest("내용" + i)))
            .collect(Collectors.toList());

        boardrpRepository.saveAll(boardrpList);

        mockMvc.perform(get("/boards/{boardId}/replys?page=1&size=5",board.getId()).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", Matchers.is(1)))
            .andExpect(jsonPath("$.size", Matchers.is(5)))
            .andExpect(jsonPath("$.totalPage", Matchers.is(5)))
            .andExpect(jsonPath("$.hasNext", Matchers.is(true)))
            .andExpect(jsonPath("$.contents[0].content").value("내용21"))
            .andExpect(jsonPath("$.contents[0].nickname").value("유저"))
            .andExpect(jsonPath("$.contents[0].boardId").value(board.getId()))
            .andDo(MockMvcResultHandlers.print());
    }

}