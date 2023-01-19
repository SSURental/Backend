package com.example.SSU_Rental.item;

import com.example.SSU_Rental.common.BaseEntity;
import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.exception.BadRequestException;
import com.example.SSU_Rental.exception.ConflictException;
import com.example.SSU_Rental.exception.ForbiddenException;
import com.example.SSU_Rental.image.ItemImage;
import com.example.SSU_Rental.item.ItemEditor.ItemEditorBuilder;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.rental.Rental;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
        cascade = {CascadeType.ALL},
        orphanRemoval = true,
        mappedBy = "item"
    )
    private List<ItemImage> itemImages = new ArrayList<>();


    private boolean isDeleted;

    @Builder
    public Item(Long id, String itemName, Group group,
        ItemStatus status, int price, Member member,boolean isDeleted) {
        this.id = id;
        this.itemName = itemName;
        this.itemGroup = group;
        this.status = status;
        this.price = price;
        this.member = member;
        this.isDeleted = isDeleted;
    }

    public static Item createItem(ItemRequest itemRequest, Member member) {
        Item item = Item.builder()
            .itemName(itemRequest.getItemName())
            .price(itemRequest.getPrice())
            .group(member.getMemberGroup())
            .status(ItemStatus.AVAILABLE)
            .member(member)
            .isDeleted(false)
            .build();

        List<ItemImage> itemImages = itemRequest.getImageDTOList().stream().map(dto -> {
            return ItemImage.builder()
                .imgName(dto.getImgName())
                .build();
        }).collect(Collectors.toList());

        for (ItemImage itemImage : itemImages) {
            item.addItemImages(itemImage);
        }
        return item;
    }

    private void addItemImages(ItemImage itemImage) {
        itemImages.add(itemImage);
        itemImage.addItem(this);
    }

    private void validate(Member member){
        if(this.member.getId()!=member.getId())
            throw new ForbiddenException();
    }


    public ItemEditorBuilder toEditor(){
        return ItemEditor.builder()
            .itemName(itemName)
            .price(price)
            .itemImages(itemImages);
    }


    public void edit(ItemEditor itemEditor,Member loginMember) {
        validate(loginMember);
        this.itemName = itemEditor.getItemName();
        this.price = itemEditor.getPrice();
        this.itemImages.clear();
        this.itemImages.addAll(itemEditor.getItemImages());
    }

    public void delete(Member loginMember){
        validate(loginMember);
//        if(this.isDeleted==true) throw new AlreadyDeletedException(); 이미 리포지터리에서 조회할 떄 삭제 여부를 검사한다.
        this.isDeleted = true;
    }


    public Rental rental(Member loginMember) {

        if(this.member.getId()==loginMember.getId()){
            throw new ConflictException();
        }

        if(status!=ItemStatus.AVAILABLE){
            throw new BadRequestException();
        }else {
            this.status = ItemStatus.LOAN;
        }


        return Rental.builder()
            .member(loginMember)
            .item(this)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(7))
            .isDeleted(false)
            .build();

    }

    public void returnItem(Rental rental,Member loginMember) {

        if(this.status!=ItemStatus.LOAN){
            throw new BadRequestException();
        }else {
            rental.delete(loginMember,this);
            this.status = ItemStatus.AVAILABLE;
        }
    }


}
