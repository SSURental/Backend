package com.example.SSU_Rental.item;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.member.Member;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ItemRepositoryCustom {

    Page<Object[]> getList(RequestPageDTO requestPageDTO);

    Page<Object[]> getMyItemList(Member member,RequestPageDTO requestPageDTO);

    List<Object[]> getItem(Long itemId);

}
