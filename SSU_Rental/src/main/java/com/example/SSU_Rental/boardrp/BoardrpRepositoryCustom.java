package com.example.SSU_Rental.boardrp;


import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.member.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BoardrpRepositoryCustom {

    Page<Boardrp> getList(Board board, RequestPageDTO requestPageDTO);

    Page<Boardrp> getMyReplyList(Member member,RequestPageDTO requestPageDTO);

    Optional<Boardrp> getBoardrp(Long boardrpId);

}
