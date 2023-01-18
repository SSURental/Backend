package com.example.SSU_Rental.rental;

import static org.junit.jupiter.api.Assertions.*;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.BadRequestException;
import com.example.SSU_Rental.exception.ConflictException;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.item.ItemRepository;
import com.example.SSU_Rental.item.ItemRequest;
import com.example.SSU_Rental.login.UserSession;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import com.example.SSU_Rental.member.MemberRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RentalServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private RentalRepository rentalRepository;
    
    @BeforeEach
    public void clear(){
        rentalRepository.deleteAll();
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }
    
    @Test
    @DisplayName("물품 1개 대여하기")
    public void test1(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "SCHOOL", "mem-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "SCHOOL", "mem-img02");
        memberRepository.save(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);

        //Act
        Long rentalId = rentalService.rental(item.getId(), createUserSession(anotherMember));
        
        //Assert
        Rental rental = rentalRepository.getRental(rentalId);
        assertEquals(rental.getId(),rentalId);
        assertEquals(rental.getMember().getId(),anotherMember.getId());
        assertEquals(rental.getItem().getId(),item.getId());
        assertEquals(rental.isDeleted(),false);
    }

    @Test
    @DisplayName("본인의 물품을 본인이 대여할 수 없습니다.")
    public void test2(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "SCHOOL", "mem-img01");
        memberRepository.save(member);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);

        //Act
        assertThrows(ConflictException.class,()->{rentalService.rental(item.getId(),createUserSession(member));});

        //Assert

    }

    @Test
    @DisplayName("이미 대여된 물품을 대여할 수 없습니다.")
    public void test3(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "SCHOOL", "mem-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "SCHOOL", "mem-img02");
        memberRepository.save(anotherMember);
        Member thirdMember = createMember("user3", "password3", "유저3", "SCHOOL", "mem-img03");
        memberRepository.save(thirdMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        Rental rental = createRental(anotherMember, item);
        rentalRepository.save(rental);
        itemRepository.save(item);
        //Act
        assertThrows( //트랜잭션이 없어 Item의 상태가 바뀌지 않음
            BadRequestException.class,()->{rentalService.rental(item.getId(),createUserSession(thirdMember));});

        //Assert

    }

    @Test
    @DisplayName("렌탈 1개 조회")
    public void test4(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "SCHOOL", "mem-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "SCHOOL", "mem-img02");
        memberRepository.save(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        Rental rental = createRental(anotherMember, item);
        rentalRepository.save(rental);
        itemRepository.save(item);

        //Act
        RentalResponse response = rentalService.getOne(item.getId(), rental.getId());


        //Assert

        assertEquals(response.getId(),rental.getId());
        assertEquals(response.getNickname(),"유저2");
        assertEquals(response.getItemName(),"아이템");
        assertEquals(response.getItemId(),item.getId());
        assertEquals(response.getImageDTO().getImgName(),"item-img01");
        assertEquals(response.getStartDate(),LocalDate.now());
        assertEquals(response.getEndDate(),LocalDate.now().plusDays(7));
    }


    @Test
    @DisplayName("렌탈 목록 페이지 조회")
    public void test5(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "SCHOOL", "mem-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "SCHOOL", "mem-img02");
        memberRepository.save(anotherMember);
        List<Item> items = IntStream.rangeClosed(1, 20).mapToObj(i -> {
            return createItem("아이템" + i, i * 1000, "img" + i, "item-image" + i, member);
        }).collect(Collectors.toList());
        itemRepository.saveAll(items);

        List<Rental> rentals = new ArrayList<>();
        for (Item item : items) {
            rentals.add(createRental(anotherMember,item));
        }
        rentalRepository.saveAll(rentals);
        itemRepository.saveAll(items);

        //Act
        ResponsePageDTO response = rentalService.getList(
            RequestPageDTO.builder().page(1).size(5).build(), createUserSession(anotherMember));

        //Assert

        assertEquals(response.getPage(),1);
        assertEquals(response.getSize(),5);
        assertEquals(response.getTotalPage(),4);
        assertEquals(response.isHasNext(), true);
        RentalResponse rentalResponse = (RentalResponse) response.getContents().get(0);
        assertEquals(rentalResponse.getItemName(),"아이템20");
        assertEquals(rentalResponse.getNickname(),"유저2");
        assertEquals(rentalResponse.getImageDTO().getImgName(),"img20");
    }

    @Test
    @DisplayName("렌탈 1개 기한 연장")
    public void test6(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "SCHOOL", "mem-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "SCHOOL", "mem-img02");
        memberRepository.save(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        Rental rental = createRental(anotherMember, item);
        rentalRepository.save(rental);
        itemRepository.save(item);

        //Act
        RentalResponse response = rentalService.extendRental(item.getId(), rental.getId(),
            createUserSession(anotherMember));

        //Assert

        assertEquals(response.getId(),rental.getId());
        assertEquals(response.getNickname(),"유저2");
        assertEquals(response.getItemName(),"아이템");
        assertEquals(response.getItemId(),item.getId());
        assertEquals(response.getImageDTO().getImgName(),"item-img01");
        assertEquals(response.getStartDate(), LocalDate.now());
        assertEquals(response.getEndDate(),LocalDate.now().plusDays(14));
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

    private Rental createRental(Member loginMember, Item item){
        return item.rental(loginMember);
    }

    private UserSession createUserSession(Member member){
        return new UserSession(member.getId());
    }

}