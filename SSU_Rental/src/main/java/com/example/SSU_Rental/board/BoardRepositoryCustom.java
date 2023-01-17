package com.example.SSU_Rental.board;


import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.member.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BoardRepositoryCustom {


    Page<Board> getList(RequestPageDTO requestPageDTO);

    Page<Board> getMyBoardList(Member member,RequestPageDTO requestPageDTO);
}
