package com.example.SSU_Rental.boardrp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardRepository;
import com.example.SSU_Rental.board.BoardRequest;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.BadRequestException;
import com.example.SSU_Rental.exception.ForbiddenException;
import com.example.SSU_Rental.exception.notfound.BoardrpNotFound;
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
class BoardrpServiceTest {


    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardrpRepository boardrpRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardrpService boardrpService;


    @BeforeEach
    public void clean(){
        boardrpRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 등록")
    public void test1(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img-01");
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);

        //Act
        Long replyId = boardrpService.register(board.getId(), new BoardrpRequest("댓글"),
            createUserSession(member));

        //Assert
        Boardrp findBoardrp = boardrpRepository.getBoardrp(replyId).get();
        assertEquals(findBoardrp.getId(),replyId);
        assertEquals(findBoardrp.getContent(),"댓글");
        assertEquals(findBoardrp.getBoard().getId(),board.getId());
        assertEquals(findBoardrp.getMember().getId(),member.getId());

    }


    @Test
    @DisplayName("댓글 페이지 조회")
    public void test2(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img-01");
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        List<Boardrp> boardrpList = IntStream.rangeClosed(1, 20)
            .mapToObj(i -> createBoardrp(board, member, "댓글" + i)).collect(
                Collectors.toList());
        boardrpRepository.saveAll(boardrpList);

        //Act
        ResponsePageDTO response = boardrpService.getList(board.getId(),
            RequestPageDTO.builder().page(1).size(5).build());

        //Assert
        BoardrpResponse boardrpResponse = (BoardrpResponse) (response.getContents().get(0));
        assertEquals(response.getPage(),1);
        assertEquals(response.getSize(),5);
        assertEquals(response.getTotalPage(),4);
        assertEquals(boardrpResponse.getContent(),"댓글20");
        assertEquals(boardrpResponse.getBoardId(),board.getId());
        assertEquals(boardrpResponse.getNickname(),"유저1");

    }

    @Test
    @DisplayName("댓글 수정하기")
    public void test3(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img-01");
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        Boardrp boardrp = createBoardrp(board, member, "내용");
        boardrpRepository.save(boardrp);

        //Act
        boardrpService.edit(board.getId(), boardrp.getId(), new BoardrpEdit("내용-수정"),createUserSession(member));

        //Assert
        Boardrp findBoardrp = getBoardrp(boardrp.getId());
        assertEquals(findBoardrp.getContent(),"내용-수정");

    }

    @Test
    @DisplayName("댓글 수정은 오직 댓글 작성자만 가능하다.")
    public void test5(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img-01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "img-02");
        memberRepository.save(anotherMember);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        Boardrp boardrp = createBoardrp(board, member, "내용");
        boardrpRepository.save(boardrp);


        //Act
        assertThrows(
            ForbiddenException.class,()->boardrpService.edit(board.getId(), boardrp.getId(), new BoardrpEdit("내용-수정"),createUserSession(anotherMember)));

    }

    @Test
    @DisplayName("댓글 수정시 댓글이 정확히 어떤 게시글의 댓글인지 URL 경로에 정확하게 표시해야 합니다.(만약 댓글과 연관된 게시글 != URL 경로상 게시글  -> 수정 실패)")
    public void test5_1(){ // 그래도 도메인 유의성이 있어 테스트함
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img-01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "img-02");
        memberRepository.save(anotherMember);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        Board anotherboard = createBoard("제목2", "내용2", member);
        boardRepository.save(anotherboard);
        Boardrp boardrp = createBoardrp(board, member, "내용");
        boardrpRepository.save(boardrp);


        //Act
        assertThrows(
            BoardrpNotFound.class,()->boardrpService.edit(anotherboard.getId(), boardrp.getId(), new BoardrpEdit("내용-수정"),createUserSession(member)));

    }


    @Test
    @DisplayName("댓글 삭제하기")
    public void test6(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img-01");
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        Boardrp boardrp = createBoardrp(board, member, "내용");
        boardrpRepository.save(boardrp);

        //Act
        boardrpService.delete(board.getId(), boardrp.getId(),createUserSession(member));

        // Assert
        Boardrp findBoardrp = boardrpRepository.findById(boardrp.getId()).get();
        assertEquals(findBoardrp.isDeleted(),true);

    }

    @Test
    @DisplayName("댓글 삭제 오직 댓글 작성자만 가능하다.")
    public void test7(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img-01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "img-02");
        memberRepository.save(anotherMember);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        Boardrp boardrp = createBoardrp(board, member, "내용");
        boardrpRepository.save(boardrp);


        //Act
        assertThrows(
            ForbiddenException.class,()->boardrpService.delete(board.getId(), boardrp.getId(),createUserSession(anotherMember)));

    }


    @Test
    @DisplayName("댓글 삭제시 댓글이 정확히 어떤 게시글의 댓글인지 URL 경로에 정확하게 표시해야 합니다.(만약 댓글과 연관된 게시글 != URL 경로상 게시글  -> 삭제 실패)")
    public void test7_1(){ // 그래도 도메인 유의성이 있어 테스트함
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img-01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "img-02");
        memberRepository.save(anotherMember);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        Board anotherboard = createBoard("제목2", "내용2", member);
        boardRepository.save(anotherboard);
        Boardrp boardrp = createBoardrp(board, member, "내용");
        boardrpRepository.save(boardrp);


        //Act
        assertThrows(
            BoardrpNotFound.class,()->boardrpService.delete(anotherboard.getId(), boardrp.getId(),createUserSession(member)));

    }

    /**
     * 이미 삭제된 댓글은 삭제 Or 수정 -> 예외 발생-> 테스트 가치가 떨어짐.
     * 애시당초 없는 댓글을 삭제 Or 수정 -> 예외발생->테스트 가치가 떨어짐
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
        return boardRepository.findById(boardId).get();
    }

    private Boardrp getBoardrp(Long boardrpId){
        return boardrpRepository.findById(boardrpId).get();
    }
}