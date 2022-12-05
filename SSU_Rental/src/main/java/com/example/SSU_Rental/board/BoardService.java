package com.example.SSU_Rental.board;

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

    @Transactional(readOnly = false)
    public Long register(BoardRequest boardRequest, Long member_id) {

        Member member = getMember(member_id);

        Board board = Board.makeBoardOne(boardRequest.getTitle(), boardRequest.getContent(),
            member);
        boardRepository.save(board);
        return board.getId();
    }

    @Transactional(readOnly = false)
    public void recommend(Long board_id) {
        Board board = getBoardOne(board_id);
        board.recommend();
    }


    @Transactional(readOnly = false)
    public void warn(Long board_id) {
        Board board = getBoardOne(board_id);
        board.warn();
    }

    @Transactional(readOnly = false)
    public BoardResponse getOne(Long board_id) {
        Board board = getBoardOne(board_id);
        board.view();
        return BoardResponse.from(board);
    }

    public ResponsePageDTO getList(RequestPageDTO requestPageDTO) {

        Pageable pageRequest = PageRequest.of(requestPageDTO.getPage() - 1,
            requestPageDTO.getSize());
        Page<Board> resultPage = boardRepository.getListPage(pageRequest);

        Function<Board, BoardResponse> fn = (entity -> BoardResponse.from(entity));
        return new ResponsePageDTO(resultPage, fn);


    }

    @Transactional(readOnly = false)
    public void modify(Long board_id, BoardRequest boardRequest, Long member_id) {

        Member member = getMember(member_id);

        Board board = validateBoard(board_id, member);
        board.modify(boardRequest.getTitle(), boardRequest.getContent());
    }

    @Transactional(readOnly = false)
    public void delete(Long board_id, Long member_id) {

        Member member = getMember(member_id);

        Board board = validateBoard(board_id, member);
        boardRepository.delete(board);
    }

    private Board getBoardOne(Long board_id) {
        Board board = boardRepository.findById(board_id)
            .orElseThrow(() -> new RuntimeException("없는 글입니다."));
        return board;
    }

    public Member getMember(Long member_id) {

        return memberRepository.findById(member_id)
            .orElseThrow(() -> new IllegalArgumentException("없는 멤버입니다."));
    }


    private Board validateBoard(Long board_id, Member member) {
        Board board = getBoardOne(board_id);

        if (board.getMember().getId() != member.getId()) {
            throw new RuntimeException("접근 권한이 없습니다.");
        }

        return board;
    }


}
