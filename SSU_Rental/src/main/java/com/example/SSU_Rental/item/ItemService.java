package com.example.SSU_Rental.item;

import static com.example.SSU_Rental.exception.ErrorMessage.*;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.exception.ErrorMessage;
import com.example.SSU_Rental.image.ItemImage;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public Long register(ItemRequest itemRequest, Long memberId) {

        Member member = getMember(memberId);
        Item item = Item.createItem(itemRequest, member);
        itemRepository.save(item);
        return item.getId();
    }

    public ResponsePageDTO getItemList(RequestPageDTO requestPageDTO) {

        Page<Object[]> pageResult = itemRepository.getListPage(ItemStatus.AVAILABLE,requestPageDTO.getGroup(), requestPageDTO.getPageable());
        Function<Object[], ItemResponse> fn = (obj -> ItemResponse.from((Item) obj[0],(List<ItemImage>)(Arrays.asList((ItemImage)obj[1]))));
        return new ResponsePageDTO(pageResult, fn);

    }


    public ItemResponse getOne(Long itemId) {

        List<Object[]> result = itemRepository.getItemWithImage(itemId);

        List<ItemImage> imageList = new ArrayList<>();
        result.forEach(arr->{
            ItemImage itemImage = (ItemImage) arr[1];
            imageList.add(itemImage);
        });


        return ItemResponse.from((Item) result.get(0)[0],imageList);
    }

//    사용하지 않은 기능 삭제
//    @Transactional(readOnly = false)
//    public void modify(Long itemId, ItemRequest itemRequest, Long memberId) {
//        Member member = getMember(memberId);
//        Item item = getItem(itemId);
//        item.validate(member);
//        item.modify(itemRequest);
//        return;
//    }

    @Transactional(readOnly = false)
    public void delete(Long itemId, Long memberId) {

        Member member = getMember(memberId);
        Item item = getItem(itemId);
        item.validate(member);
        itemRepository.delete(item);
    }



    private Member getMember(Long memberId) {

        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException((MEMBER_NOT_FOUND_ERROR)));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
            .orElseThrow(() -> new CustomException((ITEM_NOT_FOUND_ERROR)));
    }


}
