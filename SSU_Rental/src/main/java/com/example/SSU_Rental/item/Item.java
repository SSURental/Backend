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
@Table(name = "item")
@Entity
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String item_name;

    @Enumerated(EnumType.STRING)
    private Group item_group;

    @Enumerated(EnumType.STRING)
    private ItemStatus item_status;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY) //다대일 관계 -> 여러 item에 주인 한명
    @JoinColumn(name = "member_id")
    private Member item_owner;

    @OneToMany(
        cascade = {CascadeType.PERSIST,CascadeType.REMOVE},
        orphanRemoval = true,
        mappedBy = "item"
    )
    private List<ItemImage> itemImages = new ArrayList<>();

    @Builder
    public Item(Long id, String item_name, Group item_group,
        ItemStatus item_status, int price, Member item_owner,List<ItemImage> itemImages) {
        this.id = id;
        this.item_name = item_name;
        this.item_group = item_group;
        this.item_status = item_status;
        this.price = price;
        this.item_owner = item_owner;
    }

    public static Item makeItemOne(ItemRequest itemRequest, Member member) {
        Item item = Item.builder()
            .item_name(itemRequest.getName())
            .price(itemRequest.getPrice())
            .item_group(member.getGroup())
            .item_status(ItemStatus.AVAILABLE)
            .item_owner(member)
            .build();

        List<ItemImage> itemImages = itemRequest.getImageDTOList().stream().map(dto -> {
            return ItemImage.builder()
                .path(dto.getFolderPath())
                .uuid(dto.getUuid())
                .imgName(dto.getFileName())
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
        this.item_name = itemRequest.getName();
        this.price = itemRequest.getPrice();
    }



}
