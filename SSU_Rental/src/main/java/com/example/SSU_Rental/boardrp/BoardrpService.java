package com.example.SSU_Rental.boardrp;

import com.example.SSU_Rental.PageResponse;
import com.example.SSU_Rental.RequestPageDTO;
import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardRepository;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardrpService {

    private final BoardrpRepository boardrpRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;


    @Transactional(readOnly = false)
    public Long register(Long board_id, BoardrpRequest request){

        Member member = memberRepository.findById(request.getMember_id())
            .orElseThrow(() -> new RuntimeException("없는 멤버입니다."));

        Board board = boardRepository.findById(board_id)
            .orElseThrow(() -> new RuntimeException("없는 글입니다."));

        Boardrp boardrp = Boardrp.builder()
            .content(request.getContent())
            .member(member)
            .board(board)
            .build();

        boardrpRepository.save(boardrp);

        return boardrp.getId();


    }

    public PageResponse getList(Long board_id, RequestPageDTO requestPageDTO){
        PageRequest request = PageRequest.of(requestPageDTO.getPage() - 1,
            requestPageDTO.getSize());

        Board board = boardRepository.findById(board_id)
            .orElseThrow(() -> new RuntimeException("없는 글입니다."));

        Page<Boardrp> result = boardrpRepository.findByBoard(board,request);
        Function<Boardrp, BoardrpResponse> fn = (entity -> BoardrpResponse.from(entity));

        return new PageResponse(result, fn);
    }

    @Transactional(readOnly = false)
    public void modify(Long board_id, Long reply_id, BoardrpRequest request) {
        Boardrp boardrp = validateReply(board_id,reply_id,request.getMember_id());

        boardrp.modify(request.getContent());
        return;


    }

    @Transactional(readOnly = false)
    public void delete(Long board_id, Long reply_id, Long member_id) {
        Boardrp boardrp = validateReply(board_id, reply_id, member_id);

        boardrpRepository.delete(boardrp);
        return;
    }

    public Boardrp validateReply(Long board_id,Long reply_id,Long member_id){
        Boardrp boardrp = boardrpRepository.findById(reply_id)
            .orElseThrow(() -> new RuntimeException("없는 댓글입니다."));

        if(boardrp.getBoard().getId()!=board_id){
            throw new RuntimeException("없는 댓글입니다.");
        }

        if(boardrp.getMember().getMember_id()!=member_id){
            throw new RuntimeException("수정할 권한이 없습니다.");
        }

        return  boardrp;
    }
}
