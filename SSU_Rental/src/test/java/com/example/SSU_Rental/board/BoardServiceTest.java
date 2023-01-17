package com.example.SSU_Rental.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.login.UserSession;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import com.example.SSU_Rental.member.MemberRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;


    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void clean(){
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    
    @Test
    @DisplayName("게시글 작성")
    public void test1(){
        Member  member = createMember(new MemberRequest("user1","password1","유저1","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(member);

        Long boardId = boardService.register(new BoardRequest("제목", "내용"),
            createUserSession(member.getId()));

        assertEquals(boardRepository.count(),1L);
        assertEquals(boardRepository.findAll().get(0).getId(),boardId);
        assertEquals(boardRepository.findAll().get(0).getTitle(),"제목");
        assertEquals(boardRepository.findAll().get(0).getContent(),"내용");
        assertEquals(boardRepository.findAll().get(0).getMember().getId(),member.getId()); //Spring Data jpa 모든 메소드에 @Transactional 적용 -> 프록시 객체 초기화 가능
    }

    @Test
    @DisplayName("게시글 1개 조회")
    public void test2(){
        Member  member = createMember(new MemberRequest("user1","password1","유저1","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(member);
        Board board = createBoard(new BoardRequest("제목","내용"),member);
        boardRepository.save(board);

        BoardResponse response = boardService.getOne(board.getId());

        assertEquals(response.getTitle(),"제목");
        assertEquals(response.getContent(),"내용");
        assertEquals(response.getNickname(),"유저1");
        assertEquals(response.getViews(),1);
        assertEquals(response.getLikes(),0);
        assertEquals(response.getDislikes(),0);

    }


    @Test
    @DisplayName("없는 게시글을 조회할 수 없습니다.")
    public void test3(){
        Member  member = createMember(new MemberRequest("user1","password1","유저1","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(member);
        Board board = createBoard(new BoardRequest("제목","내용"),member);
        boardRepository.save(board);

        assertThrows(CustomException.class,()->{boardService.getOne(board.getId()+10L);});

    }


    @Test
    @DisplayName("게시글 1개 좋아요")
    public void test4(){
        Member  member = createMember(new MemberRequest("user1","password1","유저1","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(member);
        Board board = createBoard(new BoardRequest("제목","내용"),member);
        boardRepository.save(board);

        boardService.like(board.getId());

        Board findBoard = getBoard(board.getId());
        assertEquals(findBoard.getLikes(),board.getLikes()+1);
    }


    @Test
    @DisplayName("게시글 1개 싫어요")
    public void test5(){
        Member  member = createMember(new MemberRequest("user1","password1","유저1","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(member);
        Board board = createBoard(new BoardRequest("제목","내용"),member);
        boardRepository.save(board);

        boardService.dislike(board.getId());

        Board findBoard = getBoard(board.getId());
        assertEquals(findBoard.getDislikes(),board.getDislikes()+1);
    }


    @Test
    @DisplayName("게시글 1개 신고")
    public void test6(){
        Member  member = createMember(new MemberRequest("user1","password1","유저1","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(member);
        Board board = createBoard(new BoardRequest("제목","내용"),member);
        boardRepository.save(board);

        boardService.warn(board.getId());

        Board findBoard = getBoard(board.getId());
        assertEquals(findBoard.getWarns(),board.getWarns()+1);

    }


    @Test
    @DisplayName("게시글 1페이지 조회")
    public void test7(){
        Member  member = createMember(new MemberRequest("user1","password1","유저1","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(member);
        List<Board> boardList = IntStream.rangeClosed(1, 20)
            .mapToObj(i -> createBoard(new BoardRequest("제목"+i,"내용"+i),member)).collect(
                Collectors.toList());
        boardRepository.saveAll(boardList);

        ResponsePageDTO response = boardService.getList(
            RequestPageDTO.builder().page(1).size(5).build());

        assertEquals(response.getPage(),1);
        assertEquals(response.getSize(),5);
        assertEquals(response.isHasNext(),true);
        assertEquals(response.getTotalPage(),4);
        BoardResponse boardResponse = (BoardResponse) (response.getContents().get(0));
        assertEquals(boardResponse.getTitle(),"제목20");
        assertEquals(boardResponse.getContent(),"내용20");
        assertEquals(boardResponse.getNickname(),"유저1");
    }

    @Test
    @DisplayName("게시글 1개 수정 ")
    public void test8(){
        Member  member = createMember(new MemberRequest("user1","password1","유저1","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(member);
        Board board = createBoard(new BoardRequest("제목","내용"),member);
        boardRepository.save(board);

        boardService.edit(board.getId(), new BoardEdit("제목-수정",""),createUserSession(member.getId()));

        Board findBoard = getBoard(board.getId());
        assertEquals(findBoard.getTitle(),"제목-수정");
        assertEquals(findBoard.getContent(),"내용");
    }

    @Test
    @DisplayName("게시글 수정 권한이 없습니다.")
    public void test9(){
        Member  member = createMember(new MemberRequest("user1","password1","유저1","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(member);
        Member  anotherMember = createMember(new MemberRequest("user2","password2","유저2","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(anotherMember);
        Board board = createBoard(new BoardRequest("제목","내용"),member);
        boardRepository.save(board);

        assertThrows(CustomException.class,()->{boardService.edit(board.getId(), new BoardEdit("제목-수정",""),createUserSession(anotherMember.getId()));});

    }

    @Test
    @DisplayName("게시글 1개 삭제")
    public void test10(){
        Member  member = createMember(new MemberRequest("user1","password1","유저1","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(member);
        Board board = createBoard(new BoardRequest("제목","내용"),member);
        boardRepository.save(board);

        boardService.delete(board.getId(), createUserSession(member.getId()));

        assertThrows(NoSuchElementException.class,()->{boardRepository.findById(board.getId()).get();});
    }


    @Test
    @DisplayName("게시글 삭제 권한이 없습니다.")
    public void test11(){
        Member  member = createMember(new MemberRequest("user1","password1","유저1","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(member);
        Member  anotherMember = createMember(new MemberRequest("user2","password2","유저2","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(anotherMember);
        Board board = createBoard(new BoardRequest("제목","내용"),member);
        boardRepository.save(board);

        assertThrows(CustomException.class,()->{boardService.delete(board.getId(),createUserSession(anotherMember.getId()));});
    }



    @Test
    @DisplayName("존재 하지 않은 게시글을 삭제할 수 없습니다.")
    public void test12(){
        Member  member = createMember(new MemberRequest("user1","password1","유저1","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(member);
        Board board = createBoard(new BoardRequest("제목","내용"),member);
        boardRepository.save(board);

        assertThrows(CustomException.class,()->{boardService.delete(100L,createUserSession(member.getId()));});
    }

    @Test
    @DisplayName("존재 하지 않은 게시글을 수정할 수 없습니다.")
    public void test13(){
        Member  member = createMember(new MemberRequest("user1","password1","유저1","SCHOOL",new ImageDTO("hhh-111")));
        memberRepository.save(member);
        Board board = createBoard(new BoardRequest("제목","내용"),member);
        boardRepository.save(board);

        assertThrows(CustomException.class,()->{boardService.edit(100L,new BoardEdit("제목수정","내용 수정"),createUserSession(member.getId()));});
    }




    private Member createMember(MemberRequest memberRequest){
        return Member.createMember(memberRequest);
    }

    private Board createBoard(BoardRequest boardRequest,Member member){
        return Board.createBoard(boardRequest,member);
    }

    private UserSession createUserSession(Long memberId){
        return new UserSession(memberId);
    }

    private Board getBoard(Long boardId){
        return boardRepository.findById(boardId)
            .orElseThrow(()->new RuntimeException("해당 게시글은 존재하지 않습니다."));
    }


}