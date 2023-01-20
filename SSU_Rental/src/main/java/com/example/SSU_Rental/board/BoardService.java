package com.example.SSU_Rental.board;

import com.example.SSU_Rental.board.BoardEditor.BoardEditorBuilder;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.notfound.BoardNotFound;
import com.example.SSU_Rental.exception.notfound.MemberNotFound;
import com.example.SSU_Rental.login.UserSession;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {


    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long register(BoardRequest boardRequest, UserSession session) {

        Member loginMember = getMember(session.getId());
        Board board = Board.createBoard(boardRequest, loginMember);
        boardRepository.save(board);
        return board.getId();
    }

    @Transactional
    public void like(Long boardId) {
        Board board = getBoard(boardId);
        board.like();
    }

    @Transactional
    public void dislike(Long boardId) {
        Board board = getBoard(boardId);
        board.dislike();
    }


    @Transactional
    public void warn(Long boardId) {
        Board board = getBoard(boardId);
        board.warn();
    }

    @Transactional
    public BoardResponse getOne(Long boardId) {
        Board board = boardRepository.getBoard(boardId)
            .orElseThrow(() -> new BoardNotFound()); // 모든 연관관계가 필요할 떄 사용
        board.view();
        return BoardResponse.from(board);
    }

    public ResponsePageDTO getList(RequestPageDTO requestPageDTO) {

        Page<Board> resultPage = boardRepository.getList(requestPageDTO);
        Function<Board, BoardResponse> fn = (entity -> BoardResponse.from(entity));
        return new ResponsePageDTO(resultPage, fn);

    }


    @Transactional
    public void edit(Long boardId, BoardEdit editRequest, UserSession session) {

        Member loginMember = getMember(session.getId());
        Board board = getBoard(boardId);
        BoardEditorBuilder boardEditorBuilder = board.toEditor();
        BoardEditor boardEditor = boardEditorBuilder.title(editRequest.getTitle())
            .content(editRequest.getContent())
            .build();
        board.edit(boardEditor, loginMember);
    }

    @Transactional
    public void delete(Long boardId, UserSession session) {

        Member loginMember = getMember(session.getId());
        Board board = getBoard(boardId);
        board.delete(loginMember);
    }


    private Member getMember(Long memberId) {

        return memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFound());
    }

    //모든 연관관계가 필요 없을때 사용
    private Board getBoard(Long boardId) {
        Board findBoard = boardRepository.findById(boardId)
            .orElseThrow(() -> new BoardNotFound());
        if (findBoard.isDeleted()) {
            throw new BoardNotFound();
        }
        return findBoard;
    }


}
