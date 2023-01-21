package com.example.SSU_Rental.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.ForbiddenException;
import com.example.SSU_Rental.exception.notfound.BoardNotFound;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.login.UserSession;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import com.example.SSU_Rental.member.MemberRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void clean() {
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("게시글 작성")
    public void test1() {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "SCHOOL", "img-01");
        memberRepository.save(member);

        //Act
        Long boardId = boardService.register(new BoardRequest("제목", "내용"), createUserSession(member));

        //Assert
        Board findBoard = boardRepository.getBoard(boardId).get();
        assertEquals(boardRepository.count(), 1L);
        assertEquals(findBoard.getId(), boardId);
        assertEquals(findBoard.getTitle(), "제목");
        assertEquals(findBoard.getContent(), "내용");
        assertEquals(findBoard.getMember().getId(), member.getId());
    }

    @Test
    @DisplayName("게시글 1개 조회")
    public void test2() {
        //Arrange
        Member member  = createMember("user1", "password1", "유저1", "SCHOOL", "img-01");
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);

        //Act
        BoardResponse response = boardService.getOne(board.getId());

        //Assert
        assertEquals(response.getTitle(), "제목");
        assertEquals(response.getContent(), "내용");
        assertEquals(response.getNickname(), "유저1");
        assertEquals(response.getViews(), 1);
        assertEquals(response.getLikes(), 0);
        assertEquals(response.getDislikes(), 0);
    }


    @Test
    @DisplayName("없는 게시글을 조회할 수 없습니다.")
    public void test3() {
        //Arrange
        Member member  = createMember("user1", "password1", "유저1", "SCHOOL", "img-01");
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);

        //Act
        assertThrows(BoardNotFound.class, () -> {
            boardService.getOne(board.getId() + 10L);
        });

    }


    @Test
    @DisplayName("게시글 1개 좋아요")
    public void test4() {
        //Arrange
        Member member  = createMember("user1", "password1", "유저1", "SCHOOL", "img-01");
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);

        //Act
        boardService.like(board.getId());

        //Assert
        Board findBoard = getBoard(board.getId());
        assertEquals(findBoard.getLikes(), board.getLikes() + 1);
    }


    @Test
    @DisplayName("게시글 1개 싫어요")
    public void test5() {
        //Arrange
        Member member  = createMember("user1", "password1", "유저1", "SCHOOL", "img-01");
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);

        //Act
        boardService.dislike(board.getId());

        //Assert
        Board findBoard = getBoard(board.getId());
        assertEquals(findBoard.getDislikes(), board.getDislikes() + 1);
    }


    @Test
    @DisplayName("게시글 1개 신고")
    public void test6() {
        //Arrange
        Member member  = createMember("user1", "password1", "유저1", "SCHOOL", "img-01");
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);

        //Act
        boardService.warn(board.getId());

        //Assert
        Board findBoard = getBoard(board.getId());
        assertEquals(findBoard.getWarns(), board.getWarns() + 1);
    }


    @Test
    @DisplayName("게시글 1페이지 조회")
    public void test7() {
        //Arrange
        Member member  = createMember("user1", "password1", "유저1", "SCHOOL", "img-01");
        memberRepository.save(member);
        List<Board> boardList = IntStream.rangeClosed(1, 20)
            .mapToObj(i -> createBoard("제목" + i, "내용" + i, member)).collect(
                Collectors.toList());
        boardRepository.saveAll(boardList);

        //Act
        ResponsePageDTO response = boardService.getList(
            RequestPageDTO.builder().page(1).size(5).build());

        //Arrange
        assertEquals(response.getPage(), 1);
        assertEquals(response.getSize(), 5);
        assertEquals(response.isHasNext(), true);
        assertEquals(response.getTotalPage(), 4);
        BoardResponse boardResponse = (BoardResponse) (response.getContents().get(0));
        assertEquals(boardResponse.getTitle(), "제목20");
        assertEquals(boardResponse.getContent(), "내용20");
        assertEquals(boardResponse.getNickname(), "유저1");
    }

    @Test
    @DisplayName("게시글 1개 수정 ")
    public void test8() {
        //Arrange
        Member member  = createMember("user1", "password1", "유저1", "SCHOOL", "img-01");
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);

        //Act
        boardService.edit(board.getId(), new BoardEdit("제목-수정", ""), createUserSession(member));

        //Assert
        Board findBoard = getBoard(board.getId());
        assertEquals(findBoard.getTitle(), "제목-수정");
        assertEquals(findBoard.getContent(), "내용");
    }

    @Test
    @DisplayName("게시글 수정 권한이 없습니다.")
    public void test9() {
        //Arrange
        Member member  = createMember("user1", "password1", "유저1", "SCHOOL", "img-01");
        memberRepository.save(member);
        Member anotherMember  = createMember("user2", "password2", "유저2", "SCHOOL", "img-02");
        memberRepository.save(anotherMember);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);

        //Act
        assertThrows(ForbiddenException.class, () -> {
            boardService.edit(board.getId(), new BoardEdit("제목-수정", ""), createUserSession(anotherMember));
        });
    }

    @Test
    @DisplayName("게시글 1개 삭제")
    public void test10() {
        //Arrange
        Member member  = createMember("user1", "password1", "유저1", "SCHOOL", "img-01");
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);

        //Act
        boardService.delete(board.getId(), createUserSession(member));

        Board findBoard = boardRepository.findById(board.getId()).get();
        assertEquals(findBoard.isDeleted(),true);
    }


    @Test
    @DisplayName("게시글 삭제 권한이 없습니다.")
    public void test11() {
        //Arrange
        Member member  = createMember("user1", "password1", "유저1", "SCHOOL", "img-01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "SCHOOL", "img-02");
        memberRepository.save(anotherMember);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);

        //Act
        assertThrows(ForbiddenException.class, () -> {
            boardService.delete(board.getId(), createUserSession(anotherMember));
        });
    }


    /**
     * 도메인 로직이 아니므로 단위 테스트의 가치가 떨어짐.
      */
//    @Test
//    @DisplayName("존재 하지 않은 게시글을 삭제할 수 없습니다.")
//    public void test12() {
//        Member member  = createMember("user1", "password1", "유저1", "SCHOOL", "img-01");
//        memberRepository.save(member);
//        Board board = createBoard("제목", "내용", member);
//        boardRepository.save(board);
//
//        assertThrows(CustomException.class, () -> {
//            boardService.delete(100L, createUserSession(member.getId()));
//        });
//    }


    /**
     * 도메인 로직이 아니므로 단위 테스트의 가치가 떨어짐.
     */
//    @Test
//    @DisplayName("존재 하지 않은 게시글을 수정할 수 없습니다.")
//    public void test13() {
//        Member member = createMember(
//            new MemberRequest("user1", "password1", "유저1", "SCHOOL", new ImageDTO("hhh-111")));
//        memberRepository.save(member);
//        Board board = createBoard(new BoardRequest("제목", "내용"), member);
//        boardRepository.save(board);
//
//        assertThrows(CustomException.class, () -> {
//            boardService.edit(100L, new BoardEdit("제목수정", "내용 수정"),
//                createUserSession(member.getId()));
//        });
//    }



    /**
     * 이미 삭제된 글을 삭제 Or 수정-> 예외발생 -> 테스트 가치가 떨어짐.
     * 애시당초 없는 글을 삭제 Or 수정 -> 예외발생 ->테스트 가치가 떨어짐.
     */



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

    private Board createBoard(String title, String content, Member member) {
        return Board.createBoard(new BoardRequest(title, content), member);
    }

    private UserSession createUserSession(Member member) {
        return new UserSession(member.getId());
    }

    private Board getBoard(Long boardId) {
        return boardRepository.getBoard(boardId).orElseThrow(()->new BoardNotFound());
    }


}