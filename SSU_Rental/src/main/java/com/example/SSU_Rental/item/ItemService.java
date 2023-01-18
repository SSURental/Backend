package com.example.SSU_Rental.item;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.AlreadyDeletedException;
import com.example.SSU_Rental.exception.notfound.ItemNotFound;
import com.example.SSU_Rental.exception.notfound.MemberNotFound;
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

    @Transactional
    public Long register(ItemRequest itemRequest, UserSession session) {

        Member loginMember = getMember(session.getId());
        Item item = Item.createItem(itemRequest, loginMember);
        itemRepository.save(item);
        return item.getId();
    }


    public ItemResponse getOne(Long itemId) {
        Item item = itemRepository.getItem(itemId);
        return ItemResponse.from(item, item.getItemImages());
    }

    public ResponsePageDTO getItemList(RequestPageDTO requestPageDTO) {

        Page<Object[]> pageResult = itemRepository.getList(requestPageDTO);
        Function<Object[], ItemResponse> fn = (obj -> ItemResponse.from((Item) obj[0],
            Arrays.asList((ItemImage) obj[1])));
        return new ResponsePageDTO(pageResult, fn);
    }



    @Transactional
    public void edit(Long itemId, ItemEdit editRequest, UserSession session) {
        Member loginMember = getMember(session.getId());
        Item item = getItem(itemId);
        ItemEditorBuilder itemEditorBuilder = item.toEditor();

        List<ItemImage> itemImages = editRequest.getImageDTOList().stream().map(imageDTO -> {
            return new ItemImage(imageDTO.getImgName(),item);
        }).collect(Collectors.toList());

        ItemEditor itemEditor = itemEditorBuilder.itemName(editRequest.getItemName())
            .price(editRequest.getPrice())
            .itemImages(itemImages)
            .build();

        item.edit(itemEditor,loginMember);
        return;
    }

    @Transactional
    public void delete(Long itemId, UserSession session) {

        Member loginMember = getMember(session.getId());
        Item item = getItem(itemId);
        item.delete(loginMember);
    }


    private Member getMember(Long memberId) {

        return memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFound());
    }

    // 간단한 조회일때는 findById, 연관관계 다 끌고 와야 할때는 Itemrepository.getItem();
    private Item getItem(Long itemId) {
        Item findItem = itemRepository.findById(itemId)
            .orElseThrow(() -> new ItemNotFound());
        if(findItem.isDeleted()) throw new AlreadyDeletedException();
        return findItem;
    }



}
