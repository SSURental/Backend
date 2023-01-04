package com.example.SSU_Rental.item;

import static com.example.SSU_Rental.exception.ErrorMessage.ITEM_NOT_FOUND_ERROR;
import static com.example.SSU_Rental.exception.ErrorMessage.MEMBER_NOT_FOUND_ERROR;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.image.ItemImage;
import com.example.SSU_Rental.item.ItemEditor.ItemEditorBuilder;
import com.example.SSU_Rental.login.UserSession;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = false)
    public Long register(ItemRequest itemRequest, UserSession session) {

        Member member = getMember(session.getId());
        Item item = Item.createItem(itemRequest, member);
        itemRepository.save(item);
        return item.getId();
    }

    public ResponsePageDTO getItemList(RequestPageDTO requestPageDTO) {

        Page<Object[]> pageResult = itemRepository.getList(requestPageDTO);
        Function<Object[], ItemResponse> fn = (obj -> ItemResponse.from((Item) obj[0],
            Arrays.asList((ItemImage) obj[1])));
        return new ResponsePageDTO(pageResult, fn);
    }


    public ItemResponse getOne(Long itemId) {
        List<Object[]> result = itemRepository.getItem(itemId);
        return ItemResponse.from((Item) result.get(0)[0], (List<ItemImage>) result.get(0)[1]);
    }

    @Transactional
    public void edit(Long itemId, ItemEdit editRequest, UserSession session) {
        Member member = getMember(session.getId());
        Item item = getItem(itemId);
        item.validate(member);
        ItemEditorBuilder itemEditorBuilder = item.toEditor();

        List<ItemImage> itemImages = editRequest.getImageDTOList().stream().map(imageDTO -> {
            return new ItemImage(imageDTO.getImgName(),item);
        }).collect(Collectors.toList());

        ItemEditor itemEditor = itemEditorBuilder.itemName(editRequest.getItemName())
            .price(editRequest.getPrice())
            .itemImages(itemImages)
            .build();

        item.edit(itemEditor);
        return;
    }

    @Transactional
    public void delete(Long itemId, UserSession session) {

        Member member = getMember(session.getId());
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
