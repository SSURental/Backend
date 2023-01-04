package com.example.SSU_Rental.boardrp;

import static com.example.SSU_Rental.exception.ErrorMessage.BOARD_NOT_FOUND_ERROR;
import static com.example.SSU_Rental.exception.ErrorMessage.MEMBER_NOT_FOUND_ERROR;
import static com.example.SSU_Rental.exception.ErrorMessage.REPLY_NOT_FOUND_ERROR;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardRepository;
import com.example.SSU_Rental.boardrp.BoardrpEditor.BoardrpEditorBuilder;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardrpService {

    private final BoardrpRepository boardrpRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long register(Long boardId, BoardrpRequest boardrpRequest, UserSession session) {

        Member member = getMember(session.getId());
        Board board = getBoard(boardId);
        Boardrp boardrp = Boardrp.createBoardrp(board, member, boardrpRequest);
        boardrpRepository.save(boardrp);
        board.addBoard(boardrp);
        return boardrp.getId();

    }

    public ResponsePageDTO getList(Long boardId, RequestPageDTO requestPageDTO) {
        Board board = getBoard(boardId);
        Page<Boardrp> pageResult = boardrpRepository.getList(board, requestPageDTO);
        Function<Boardrp, BoardrpResponse> fn = (entity -> BoardrpResponse.from(entity));
        return new ResponsePageDTO(pageResult, fn);
    }

    @Transactional
    public void edit(Long boardId, Long replyId, BoardrpEdit editRequest, UserSession session) {

        Member member = getMember(session.getId());
        Boardrp boardrp = getReply(replyId);
        Board board = getBoard(boardId);
        boardrp.validate(member, board);
        BoardrpEditorBuilder boardrpEditorBuilder = boardrp.toEditor();
        BoardrpEditor editor = boardrpEditorBuilder.content(editRequest.getContent()).build();
        boardrp.edit(editor);
        return;
    }

    @Transactional
    public void delete(Long boardId, Long replyId, UserSession session) {

        Member member = getMember(session.getId());
        Boardrp boardrp = getReply(replyId);
        Board board = getBoard(boardId);
        boardrp.validate(member, board);
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
