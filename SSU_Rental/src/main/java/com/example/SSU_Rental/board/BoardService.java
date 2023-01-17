package com.example.SSU_Rental.board;

import static com.example.SSU_Rental.exception.ErrorMessage.BOARD_NOT_FOUND_ERROR;
import static com.example.SSU_Rental.exception.ErrorMessage.MEMBER_NOT_FOUND_ERROR;

import com.example.SSU_Rental.board.BoardEditor.BoardEditorBuilder;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.CustomException;
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

        Member member = getMember(session.getId());
        Board board = Board.createBoard(boardRequest, member);
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
        Board board = getBoard(boardId);
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

        Member member = getMember(session.getId());
        Board board = getBoard(boardId);
        board.validate(member);
        BoardEditorBuilder boardEditorBuilder = board.toEditor();
        BoardEditor boardEditor = boardEditorBuilder.title(editRequest.getTitle())
            .content(editRequest.getContent())
            .build();
        board.edit(boardEditor);
    }

    @Transactional
    public void delete(Long boardId, UserSession session) {

        Member member = getMember(session.getId());
        Board board = getBoard(boardId);
        board.validate(member);
        boardRepository.delete(board);
    }


    private Member getMember(Long memberId) {

        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException((MEMBER_NOT_FOUND_ERROR)));
    }

    private Board getBoard(Long boardId) {
        return boardRepository.findById(boardId)
            .orElseThrow(() -> new CustomException((BOARD_NOT_FOUND_ERROR)));
    }


}
