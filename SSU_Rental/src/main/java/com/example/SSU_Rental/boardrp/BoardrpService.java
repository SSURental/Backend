package com.example.SSU_Rental.boardrp;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardRepository;
import com.example.SSU_Rental.boardrp.BoardrpEditor.BoardrpEditorBuilder;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.AlreadyDeletedException;
import com.example.SSU_Rental.exception.notfound.BoardNotFound;
import com.example.SSU_Rental.exception.notfound.BoardrpNotFound;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardrpService {

    private final BoardrpRepository boardrpRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long register(Long boardId, BoardrpRequest boardrpRequest, UserSession session) {

        Member loginMember = getMember(session.getId());
        Board board = getBoard(boardId);
        Boardrp boardrp = Boardrp.createBoardrp(board, loginMember, boardrpRequest);
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

        Member loginMember = getMember(session.getId());
        Boardrp boardrp = getReply(replyId);
        Board board = getBoard(boardId);
        BoardrpEditorBuilder boardrpEditorBuilder = boardrp.toEditor();
        BoardrpEditor editor = boardrpEditorBuilder.content(editRequest.getContent()).build();
        boardrp.edit(editor,loginMember,board);
        return;
    }

    @Transactional
    public void delete(Long boardId, Long replyId, UserSession session) {
        Member loginMember = getMember(session.getId());
        Boardrp boardrp = getReply(replyId);
        Board board = getBoard(boardId);
        boardrp.delete(loginMember,board);
        return;
    }


    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFound());
    }

    public Board getBoard(Long boardId) {
        Board findBoard = boardRepository.findById(boardId)
            .orElseThrow(() -> new BoardNotFound());
        if(findBoard.isDeleted()) throw new AlreadyDeletedException();
        return findBoard;

    }

    public Boardrp getReply(Long replyId) {
        Boardrp findBoardrp = boardrpRepository.findById(replyId)
            .orElseThrow(() -> new BoardrpNotFound());
        if(findBoardrp.isDeleted())throw new AlreadyDeletedException();
        return findBoardrp;
    }


}
