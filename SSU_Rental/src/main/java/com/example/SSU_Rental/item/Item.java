package com.example.SSU_Rental.item;

import com.example.SSU_Rental.common.BaseEntity;
import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.image.ItemImage;
import com.example.SSU_Rental.member.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String itemName;

    @Enumerated(EnumType.STRING)
    private Group itemGroup;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY) //다대일 관계 -> 여러 item에 주인 한명
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(
        cascade = {CascadeType.PERSIST,CascadeType.REMOVE},
        orphanRemoval = true,
        mappedBy = "item"
    )
    private List<ItemImage> itemImages = new ArrayList<>();

    @Builder
    public Item(Long id, String itemName, Group group,
        ItemStatus status, int price, Member member,List<ItemImage> itemImages) {
        this.id = id;
        this.itemName = itemName;
        this.itemGroup = group;
        this.status = status;
        this.price = price;
        this.member = member;
    }

    public static Item createItem(ItemRequest itemRequest, Member member) {
        Item item = Item.builder()
            .itemName(itemRequest.getItemName())
            .price(itemRequest.getPrice())
            .group(member.getMemberGroup())
            .status(ItemStatus.AVAILABLE)
            .member(member)
            .build();

        List<ItemImage> itemImages = itemRequest.getImageDTOList().stream().map(dto -> {
            return ItemImage.builder()
                .imgName(dto.getImgName())
                .build();
        }).collect(Collectors.toList());

        for (ItemImage itemImage : itemImages) {
            item.addItem(itemImage);
        }
        return item;
    }

    private void addItem(ItemImage itemImage) {
        itemImages.add(itemImage);
        itemImage.addItem(this);
    }

    public void modify(ItemRequest itemRequest) {
        this.itemName = itemRequest.getItemName();
        this.price = itemRequest.getPrice();

    }



}
