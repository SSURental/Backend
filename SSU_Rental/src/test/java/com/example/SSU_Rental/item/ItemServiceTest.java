package com.example.SSU_Rental.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.exception.ForbiddenException;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.login.UserSession;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import com.example.SSU_Rental.member.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void clean(){
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("아이템 1개 등록")
    public void test1(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-image-01");
        memberRepository.save(member);
        ItemRequest itemRequest = ItemRequest.builder().itemName("아이템1").price(1000).build();
        itemRequest.getImageDTOList().add(new ImageDTO("item-img01"));
        itemRequest.getImageDTOList().add(new ImageDTO("item-img02"));

        //Act
        Long itemId = itemService.register(itemRequest, createUserSession(member));

        //Assert
        Item item = itemRepository.getItem(itemId);
        assertEquals(item.getId(),itemId);
        assertEquals(item.getItemName(),"아이템1");
        assertEquals(item.getPrice(),1000);
        assertEquals(item.getMember().getId(),member.getId());
        assertEquals(item.getItemGroup(), Group.STUDENT);
        assertEquals(item.isDeleted(), false);
        assertEquals(item.getItemImages().get(0).getImgName(),"item-img01");
        assertEquals(item.getItemImages().get(1).getImgName(),"item-img02");
    }

    @Test
    @DisplayName("아이템 1개 조회")
    public void test2(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-image-01");
        memberRepository.save(member);
        Item item = createItem("아이템1", 1000, "item-img01", "item-img02", member);
        itemRepository.save(item);

        //Act
        ItemResponse response = itemService.getOne(item.getId());

        //Assert

        assertEquals(response.getId(),item.getId());
        assertEquals(response.getItemName(),"아이템1");
        assertEquals(response.getPrice(),1000);
        assertEquals(response.getMemberName(),"유저1");
        assertEquals(response.getGroup(), Group.STUDENT);
        assertEquals(response.getStatus(), ItemStatus.AVAILABLE);
        assertEquals(response.getImageDTOList().get(0).getImgName(),"item-img01");
        assertEquals(response.getImageDTOList().get(1).getImgName(),"item-img02");
    }

    @Test
    @DisplayName("아이템 아이디를 잘못 입력하거나 삭제된 아이템을 조회할 수 없습니다.") // 테스트 가치가 있는지 모르겠다. 이유: 너무나 단순한 로직이여서 테스트할 가치가 없어보임
    public void test3(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-image-01");
        memberRepository.save(member);
        Item item = createItem("아이템1", 1000, "item-img01", "item-img02", member);
        itemRepository.save(item);

        //Act
        assertThrows(NullPointerException.class,()->{itemService.getOne(item.getId()+1L);});


    }

    @Test
    @DisplayName("아이템 수정.")
    public void test4(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-image-01");
        memberRepository.save(member);
        Item item = createItem("아이템1", 1000, "item-img01", "item-img02", member);
        itemRepository.save(item);

        //Act
        itemService.edit(item.getId(),ItemEdit.builder().itemName("아이템2").build(),createUserSession(member));

        //Assert
        Item findItem = itemRepository.getItem(item.getId());
        assertEquals(findItem.getId(),item.getId());
        assertEquals(findItem.getItemName(),"아이템2");
        assertEquals(findItem.getPrice(),1000);
        assertEquals(findItem.getMember().getId(),member.getId());
        assertEquals(findItem.getItemGroup(), Group.STUDENT);
        assertEquals(findItem.isDeleted(), false);
        assertEquals(findItem.getItemImages().get(0).getImgName(),"item-img01");
        assertEquals(findItem.getItemImages().get(1).getImgName(),"item-img02");


    }


    @Test
    @DisplayName("권한 없이 아이템 수정을 할 수 없습니다.")
    public void test5(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-image-01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-image-02");
        memberRepository.save(anotherMember);
        Item item = createItem("아이템1", 1000, "item-img01", "item-img02", member);
        itemRepository.save(item);

        //Act
        assertThrows(ForbiddenException.class,()->{itemService.edit(item.getId(),ItemEdit.builder().itemName("아이템2").build(),createUserSession(anotherMember));});

    }


    @Test
    @DisplayName("아이템 삭제")
    public void test6(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-image-01");
        memberRepository.save(member);
        Item item = createItem("아이템1", 1000, "item-img01", "item-img02", member);
        itemRepository.save(item);

        //Act
        itemService.delete(item.getId(),createUserSession(member));

        //Arrange
        Item findItem = itemRepository.findById(item.getId()).get();
        assertEquals(findItem.isDeleted(),true);

    }

    @Test
    @DisplayName("권한없이 아이템을 삭제할 수 없습니다.")
    public void test7(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-image-01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-image-02");
        memberRepository.save(anotherMember);
        Item item = createItem("아이템1", 1000, "item-img01", "item-img02", member);
        itemRepository.save(item);

        //Act
        assertThrows(ForbiddenException.class,()->{itemService.delete(item.getId(),createUserSession(anotherMember));});

    }




    private Member createMember(String loginId, String password,String name,String group, String imageName){
        return Member.createMember(new MemberRequest(loginId,password,name,group,new ImageDTO(imageName)));
    }

    private Item createItem(String itemName,int price,String imgName1,String imgName2,Member member){
        ItemRequest itemRequest = new ItemRequest(itemName, price);
        itemRequest.getImageDTOList().add(new ImageDTO(imgName1));
        itemRequest.getImageDTOList().add(new ImageDTO(imgName2));
        return Item.createItem(itemRequest,member);
    }

    private UserSession createUserSession(Member member){
        return new UserSession(member.getId());
    }
}