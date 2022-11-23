package com.example.SSU_Rental.item;

import com.example.SSU_Rental.boardrp.Boardrp;
import com.example.SSU_Rental.boardrp.BoardrpResponse;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.member.Member;
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
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = false)
    public Long register(ItemRequest itemRequest, Long member_id) {

        Member member = getMember(member_id);

        Item item = Item.makeItemOne(itemRequest, member);
        itemRepository.save(item);
        return item.getId();
    }

    public ResponsePageDTO getItemList(RequestPageDTO requestPageDTO) {

        Pageable pageRequest = PageRequest.of(requestPageDTO.getPage() - 1,
            requestPageDTO.getSize());
        Page<Item> pageResult = itemRepository.findByItem_statusAndAndItem_group(
            ItemStatus.AVAILABLE, requestPageDTO.getGroup(), pageRequest);
        Function<Item, ItemResponse> fn = (entity -> ItemResponse.from(entity));
        return new ResponsePageDTO(pageResult, fn);

    }

    @Transactional(readOnly = false)
    public void modify(Long item_id, ItemRequest itemRequest, Long member_id) {
        Member member = getMember(member_id);
        Item item = getItem(item_id);
        validateItem(item, member);
        item.modify(itemRequest);
        return;
    }

    @Transactional(readOnly = false)
    public void delete(Long item_id, Long member_id) {

        Member member = getMember(member_id);
        Item item = getItem(item_id);

        validateItem(item, member);
        itemRepository.delete(item);
    }

    private void validateItem(Item item, Member member) {
        if (item.getItem_owner().getMember_id() != member.getMember_id()) {
            throw new IllegalArgumentException("없는 권한입니다.");
        }
    }


    private Member getMember(Long member_id) {

        return memberRepository.findById(member_id)
            .orElseThrow(() -> new IllegalArgumentException("없는 멤버입니다."));
    }

    private Item getItem(Long item_id) {
        return itemRepository.findById(item_id)
            .orElseThrow(() -> new IllegalArgumentException("없는 아이템 입니다."));
    }


}
