package com.example.SSU_Rental.board;

import com.example.SSU_Rental.PageResponse;
import com.example.SSU_Rental.RequestPageDTO;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
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
    private final MemberRepository memberRepository;




    @Transactional(readOnly = false)
    public Long register(BoardRequest boardRequest){
        Member member = validateMember(boardRequest.getMember_id());

        Board board = Board.builder()
            .title(boardRequest.getTitle())
            .content(boardRequest.getContent())
            .member(member)
            .see_cnt(0L)
            .rec_cnt(0L)
            .warn_cnt(0L)
            .build();

        boardRepository.save(board);
        return board.getId();

    }

    @Transactional(readOnly = false)
    public BoardResponse getOne(Long board_id){
        Board board = boardRepository.findById(board_id)
            .orElseThrow(() -> new IllegalArgumentException("없는 글입니다."));

        board.view();
        return board.makeResponse();
    }


    @Transactional(readOnly = false)
    public void modify(Long board_id,BoardRequest request){
        Board board = validateBoard(board_id, request.getMember_id());

        board.modify(request.getTitle(), request.getContent());

        return;

    }


    public PageResponse getList(RequestPageDTO requestPageDTO) {
        PageRequest request = PageRequest.of(requestPageDTO.getPage() - 1,
            requestPageDTO.getSize());

        Page<Board> result = boardRepository.getListPage(request);
        Function<Board, BoardResponse> fn = (entity -> BoardResponse.from(entity));

        return new PageResponse(result, fn);
    }

    @Transactional(readOnly = false)
    public void delete(Long board_id, Long member_id) {
        Board board = validateBoard(board_id, member_id);

        boardRepository.delete(board);
        return;
    }



    @Transactional(readOnly = false)
    public void recommend(Long board_id,Long member_id) {
        Board board = boardRepository.findById(board_id)
            .orElseThrow(() -> new RuntimeException("없는 글입니다."));

        validateMember(member_id);

        board.recommend();
    }

    @Transactional(readOnly = false)
    public void warn(Long board_id,Long member_id) {
        Board board = boardRepository.findById(board_id)
            .orElseThrow(() -> new RuntimeException("없는 글입니다."));


        validateMember(member_id);

        board.warn();
    }


    private Board validateBoard(Long board_id,Long member_id){
        Board board = boardRepository.findById(board_id)
            .orElseThrow(() -> new RuntimeException("없는 글입니다."));

        if(board.getMember().getMember_id()!=member_id){
            throw new RuntimeException("삭제할 권한이 없습니다.");
        }

        return board;
    }

    private Member validateMember(Long member_id){
        return memberRepository.findById(member_id)
            .orElseThrow(() -> new IllegalArgumentException("없는 멤버입니다."));
    }
}
