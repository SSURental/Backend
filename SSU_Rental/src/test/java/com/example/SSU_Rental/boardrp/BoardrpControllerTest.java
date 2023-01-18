package com.example.SSU_Rental.boardrp;

import com.example.SSU_Rental.board.BoardRepository;
import com.example.SSU_Rental.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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



//    @Test
//    @DisplayName("댓글 페이지 테스트")
//    public void test1() throws Exception {
//
//        Member member = Member.builder()
//            .loginId("user1")
//            .password("password1")
//            .memberGroup(Group.STUDENT)
//            .name("유저")
//            .build();
//
//        memberRepository.save(member);
//
//        Board board = Board.createBoard(new BoardRequest("제목", "내용"), member);
//        boardRepository.save(board);
//
//        List<Boardrp> boardrpList = IntStream.rangeClosed(1, 21)
//            .mapToObj(i -> Boardrp.createBoardrp(board, member, new BoardrpRequest("내용" + i)))
//            .collect(Collectors.toList());
//
//        boardrpRepository.saveAll(boardrpList);
//
//        mockMvc.perform(get("/boards/{boardId}/replys?page=1&size=5",board.getId()).contentType(APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.page", Matchers.is(1)))
//            .andExpect(jsonPath("$.size", Matchers.is(5)))
//            .andExpect(jsonPath("$.totalPage", Matchers.is(5)))
//            .andExpect(jsonPath("$.hasNext", Matchers.is(true)))
//            .andExpect(jsonPath("$.contents[0].content").value("내용21"))
//            .andExpect(jsonPath("$.contents[0].nickname").value("유저"))
//            .andExpect(jsonPath("$.contents[0].boardId").value(board.getId()))
//            .andDo(MockMvcResultHandlers.print());
//    }

}