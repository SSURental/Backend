package com.example.SSU_Rental.boardrp;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardRepository;
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
    public Long register(Long board_id, BoardrpRequest boardrpRequest, Long member_id) {

        Member member = getMember(member_id);

        Board board = boardRepository.findById(board_id)
            .orElseThrow(() -> new RuntimeException("없는 글입니다."));

        Boardrp boardrp = Boardrp.builder()
            .content(boardrpRequest.getContent())
            .board(board)
            .member(member)
            .build();

        boardrpRepository.save(boardrp);
        return boardrp.getId();

    }

    public ResponsePageDTO getList(Long board_id, RequestPageDTO requestPageDTO) {
        Board board = boardRepository.findById(board_id)
            .orElseThrow(() -> new RuntimeException("없는 글입니다."));

        Pageable pageRequest = PageRequest.of(requestPageDTO.getPage() - 1,
            requestPageDTO.getSize());

        Page<Boardrp> pageResult = boardrpRepository.findByBoard(board, pageRequest);
        Function<Boardrp, BoardrpResponse> fn = (entity -> BoardrpResponse.from(entity));

        return new ResponsePageDTO(pageResult, fn);


    }

    @Transactional(readOnly = false)
    public void modify(Long board_id, Long reply_id, BoardrpRequest request, Long member_id) {

        Member member = getMember(member_id);

        Boardrp boardrp = validateReply(board_id, reply_id, member);
        boardrp.modify(request.getContent());
        return;
    }

    @Transactional(readOnly = false)
    public void delete(Long board_id, Long reply_id, Long member_id) {

        Member member = getMember(member_id);

        Boardrp boardrp = validateReply(board_id, reply_id, member);
        boardrpRepository.delete(boardrp);
        return;
    }


    public Member getMember(Long member_id) {

        return memberRepository.findById(member_id)
            .orElseThrow(() -> new IllegalArgumentException("없는 멤버입니다."));
    }


    public Boardrp validateReply(Long board_id, Long reply_id, Member member) {
        Boardrp boardrp = boardrpRepository.findById(reply_id)
            .orElseThrow(() -> new RuntimeException("없는 댓글입니다."));

        if (boardrp.getBoard().getId() != board_id) {
            throw new RuntimeException("없는 댓글입니다.");
        }

        if (boardrp.getMember().getId() != member.getId()) {
            throw new RuntimeException("수정할 권한이 없습니다.");
        }

        return boardrp;
    }


}
