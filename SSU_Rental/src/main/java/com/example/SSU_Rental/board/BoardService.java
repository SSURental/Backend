package com.example.SSU_Rental.board;

import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {


    private final BoardRepository boardRepository;

    @Transactional(readOnly = false)
    public Long register(BoardRequest boardRequest, Member member) {

        Board board = Board.makeBoardOne(boardRequest.getTitle(), boardRequest.getContent(), member);
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

        Page<Board> resultPage = boardRepository.getListPage(
            PageRequest.of(requestPageDTO.getPage() - 1,
                requestPageDTO.getSize()));

        Function<Board,BoardResponse> fn = (entity->BoardResponse.from(entity));
        return new ResponsePageDTO(resultPage,fn);


    }

    @Transactional(readOnly = false)
    public void modify(Long board_id, BoardRequest boardRequest, Member member) {
        Board board = validateBoard(board_id, member);
        board.modify(board.getTitle(), board.getContent());
    }

    @Transactional(readOnly = false)
    public void delete(Long board_id, Member member) {
        Board board = validateBoard(board_id, member);
        boardRepository.delete(board);
    }

    private Board getBoardOne(Long board_id) {
        Board board = boardRepository.findById(board_id)
            .orElseThrow(() -> new RuntimeException("없는 글입니다."));
        return board;
    }




    private Board validateBoard(Long board_id,Member member){
        Board board = getBoardOne(board_id);

        if(board.getMember().getMember_id()!= member.getMember_id()){
            throw new RuntimeException("삭제할 권한이 없습니다.");
        }

        return board;
    }


}
