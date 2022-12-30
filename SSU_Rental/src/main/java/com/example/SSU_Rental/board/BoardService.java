package com.example.SSU_Rental.board;

import static com.example.SSU_Rental.exception.ErrorMessage.*;

import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.exception.ErrorMessage;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.member.MemberRepository;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {


    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long register(BoardRequest boardRequest, Long memberId) {

        Member member = getMember(memberId);

        Board board = Board.makeBoardOne(boardRequest.getTitle(), boardRequest.getContent(),
            member);
        boardRepository.save(board);
        return board.getId();
    }

    @Transactional
    public void like(Long boardId) {
        Board board = getBoard(boardId);
        board.like();
    }

    @Transactional
    public void dislike(Long boardId){
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

    public ResponsePageDTO getBoardList(RequestPageDTO requestPageDTO) {

        Page<Board> resultPage = boardRepository.getListPage(requestPageDTO.getPageable());

        Function<Board, BoardResponse> fn = (entity -> BoardResponse.from(entity));
        return new ResponsePageDTO(resultPage, fn);

    }


    @Transactional
    public void modify(Long boardId, BoardRequest boardRequest, Long memberId) {

        Member member = getMember(memberId);
        Board board = getBoard(boardId);
        board.validate(member);
        board.modify(boardRequest.getTitle(), boardRequest.getContent());
    }

    @Transactional
    public void delete(Long boardId, Long memberId) {

        Member member = getMember(memberId);
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
