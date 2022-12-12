package com.example.SSU_Rental.boardrp;

import static com.example.SSU_Rental.exception.ErrorMessage.*;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardRepository;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardrpService {

    private final BoardrpRepository boardrpRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = false)
    public Long register(Long boardId, BoardrpRequest boardrpRequest, Long memberId) {

        Member member = getMember(memberId);

        Board board = getBoard(boardId);

        Boardrp boardrp = Boardrp.createBoardrp(board, member, boardrpRequest);

        boardrpRepository.save(boardrp);
        return boardrp.getId();

    }

    public ResponsePageDTO getReplyList(Long boardId, RequestPageDTO requestPageDTO) {
        Board board = getBoard(boardId);

        Page<Boardrp> pageResult = boardrpRepository.findByBoard(board,
            requestPageDTO.getPageable());
        Function<Boardrp, BoardrpResponse> fn = (entity -> BoardrpResponse.from(entity));

        return new ResponsePageDTO(pageResult, fn);


    }

//    사용하지 않은 기능 삭제
//    @Transactional
//    public void modify(Long boardId,Long replyId, BoardrpRequest request, Long member_id) {
//
//        Member member = getMember(member_id);
//        Boardrp boardrp = getReply(replyId);
//        Board board = getBoard(boardId);
//        boardrp.validate(member,board);
//        boardrp.modify(request.getContent());
//        return;
//    }

    @Transactional
    public void delete(Long boardId,Long replyId, Long memberId) {

        Member member = getMember(memberId);
        Boardrp boardrp = getReply(replyId);
        Board board = getBoard(boardId);
        boardrp.validate(member,board);
        boardrpRepository.delete(boardrp);
        return;
    }


    public Member getMember(Long memberId) {

        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException((MEMBER_NOT_FOUND_ERROR)));
    }

    public Board getBoard(Long boardId) {
        return boardRepository.findById(boardId)
            .orElseThrow(() -> new CustomException((BOARD_NOT_FOUND_ERROR)));
    }

    public Boardrp getReply(Long replyId) {
        return boardrpRepository.findById(replyId)
            .orElseThrow(() -> new CustomException((REPLY_NOT_FOUND_ERROR)));
    }


}
