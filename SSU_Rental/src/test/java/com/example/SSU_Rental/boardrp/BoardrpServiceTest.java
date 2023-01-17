package com.example.SSU_Rental.boardrp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardRepository;
import com.example.SSU_Rental.board.BoardRequest;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.login.UserSession;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import com.example.SSU_Rental.member.MemberRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardrpServiceTest {


    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardrpRepository boardrpRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardrpService boardrpService;


    @AfterEach
    public void clean(){
        boardRepository.deleteAll();
        memberRepository.deleteAll();
        boardrpRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 등록")
    public void test1(){
        Member member = createMember("user1", "password1", "유저1", "STUDENT",
            new ImageDTO("dsdadsd-sfasfsaf"));
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);

        Long replyId = boardrpService.register(board.getId(), new BoardrpRequest("댓글"),
            createUserSession(member));

        assertEquals(boardrpRepository.findAll().get(0).getId(),replyId);
        assertEquals(boardrpRepository.findAll().get(0).getContent(),"댓글");
        assertEquals(boardrpRepository.findAll().get(0).getBoard().getId(),board.getId());
        assertEquals(boardrpRepository.findAll().get(0).getMember().getId(),member.getId());

    }


    @Test
    @DisplayName("댓글 페이지 조회")
    public void test2(){
        Member member = createMember("user1", "password1", "유저1", "STUDENT",
            new ImageDTO("dsdadsd-sfasfsaf"));
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        List<Boardrp> boardrpList = IntStream.rangeClosed(1, 20)
            .mapToObj(i -> createBoardrp(board, member, "댓글" + i)).collect(
                Collectors.toList());
        boardrpRepository.saveAll(boardrpList);

        ResponsePageDTO response = boardrpService.getList(board.getId(),
            RequestPageDTO.builder().page(1).size(5).build());

        BoardrpResponse boardrpResponse = (BoardrpResponse) (response.getContents().get(0));
        assertEquals(response.getPage(),1);
        assertEquals(response.getSize(),5);
        assertEquals(response.getTotalPage(),4);
        assertEquals(boardrpResponse.getContent(),"댓글20");
        assertEquals(boardrpResponse.getBoardId(),board.getId());
        assertEquals(boardrpResponse.getNickname(),"유저1");

    }

    @Test
    @DisplayName("없는 댓글 수정하기 -> 예외 발생")
    public void test3(){
        Member member = createMember("user1", "password1", "유저1", "STUDENT",
            new ImageDTO("dsdadsd-sfasfsaf"));
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        Boardrp boardrp = createBoardrp(board, member, "내용");
        boardrpRepository.save(boardrp);

        assertThrows(CustomException.class,()-> {boardrpService.edit(board.getId(), boardrp.getId()+2L, new BoardrpEdit("내용-수정"),createUserSession(member));});

    }

    @Test
    @DisplayName("댓글 수정하기")
    public void test4(){
        Member member = createMember("user1", "password1", "유저1", "STUDENT",
            new ImageDTO("dsdadsd-sfasfsaf"));
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        Boardrp boardrp = createBoardrp(board, member, "내용");
        boardrpRepository.save(boardrp);

        boardrpService.edit(board.getId(), boardrp.getId(), new BoardrpEdit("내용-수정"),createUserSession(member));

        Boardrp findBoardrp = getBoardrp(boardrp.getId());
        assertEquals(findBoardrp.getContent(),"내용-수정");

    }

    @Test
    @DisplayName("댓글 수정은 오직 댓글 작성자만 가능하다.")
    public void test5(){
        Member member = createMember("user1", "password1", "유저1", "STUDENT",
            new ImageDTO("dsdadsd-sfasfsaf"));
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT",
            new ImageDTO("dsdadsd-sfasfsaf"));
        memberRepository.save(anotherMember);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        Boardrp boardrp = createBoardrp(board, member, "내용");
        boardrpRepository.save(boardrp);



        assertThrows(CustomException.class,()->boardrpService.edit(board.getId(), boardrp.getId(), new BoardrpEdit("내용-수정"),createUserSession(anotherMember)));

    }

    @Test
    @DisplayName("댓글 삭제하기")
    public void test6(){
        Member member = createMember("user1", "password1", "유저1", "STUDENT",
            new ImageDTO("dsdadsd-sfasfsaf"));
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        Boardrp boardrp = createBoardrp(board, member, "내용");
        boardrpRepository.save(boardrp);

        boardrpService.delete(board.getId(), boardrp.getId(),createUserSession(member));
        // todo

    }



    private Member createMember(String loginId, String password,String name,String group, ImageDTO imageDTO){
        return Member.createMember(new MemberRequest(loginId,password,name,group,imageDTO));
    }

    private Board createBoard(String title,String content,Member member){
        return Board.createBoard(new BoardRequest(title,content),member);
    }

    private Boardrp createBoardrp(Board board,Member member,String content){
        return Boardrp.createBoardrp(board, member, new BoardrpRequest(content));
    }

    private UserSession createUserSession(Member member){
        return new UserSession(member.getId());
    }

    private Board getBoard(Long boardId){
        return boardRepository.findById(boardId)
            .orElseThrow(()->new RuntimeException("해당 게시글은 존재하지 않습니다."));
    }

    private Boardrp getBoardrp(Long boardrpId){
        return boardrpRepository.findById(boardrpId)
            .orElseThrow(()->new RuntimeException("해당 게시글은 존재하지 않습니다."));
    }
}