package com.example.SSU_Rental.item;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.member.Member;
import org.springframework.data.domain.Page;

public interface ItemRepositoryCustom {

    Page<Item> getList(RequestPageDTO requestPageDTO);

    Page<Item> getMyItemList(Member member,RequestPageDTO requestPageDTO);

    Item getItem(Long itemId);

}
