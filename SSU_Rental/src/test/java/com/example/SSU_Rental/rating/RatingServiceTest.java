package com.example.SSU_Rental.rating;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.BadRequestException;
import com.example.SSU_Rental.exception.ConflictException;
import com.example.SSU_Rental.exception.ForbiddenException;
import com.example.SSU_Rental.exception.notfound.RatingNotFound;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.item.ItemRepository;
import com.example.SSU_Rental.item.ItemRequest;
import com.example.SSU_Rental.login.UserSession;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import com.example.SSU_Rental.member.MemberRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RatingServiceTest {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void clear(){ // 삭제 순서 조심(참조로 인해 삭제가 안되는 경우 발생)
        ratingRepository.deleteAll();
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("리뷰 등록")
    public void test1(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-img02");
        memberRepository.save(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);

        //Act
        Long ratingId = ratingService.register(item.getId(), new RatingRequest(10, "좋아요"),
            createUserSession(anotherMember));

        //Assert
        Rating rating = ratingRepository.getRating(ratingId).get();
        assertEquals(rating.getId(),ratingId);
        assertEquals(rating.getScore(),10);
        assertEquals(rating.getContent(),"좋아요");
        assertEquals(rating.getMember().getId(),anotherMember.getId());
        assertEquals(rating.getItem().getId(),item.getId());
    }

    @Test
    @DisplayName("본인의 아이템에 본인이 리뷰를 등록할 수 없습니다.")
    public void test2(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);

        //Act
        assertThrows(ConflictException.class,()->{ratingService.register(item.getId(), new RatingRequest(10, "좋아요"),
            createUserSession(member));});
    }

    @Test
    @DisplayName("특정 아이템의 리뷰 평점 확인")
    public void test3(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-img02");
        memberRepository.save(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);

        List<Rating> ratings = IntStream.rangeClosed(1, 20).mapToObj(i -> {
            return createRating(anotherMember, item, (i % 10) + 1, "좋아요" + i);
        }).collect(Collectors.toList());
        ratingRepository.saveAll(ratings);

        //Act
        Double avgScore = ratingService.getAvgScores(item.getId());


        //Assert
        assertEquals(avgScore,5.5);
    }

    @Test
    @DisplayName("특정 아이템의 리뷰 목록 확인")
    public void test4(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-img02");
        memberRepository.save(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);

        List<Rating> ratings = IntStream.rangeClosed(1, 20).mapToObj(i -> {
            return createRating(anotherMember, item, (i % 10) + 1, "좋아요" + i);
        }).collect(Collectors.toList());
        ratingRepository.saveAll(ratings);

        //Act
        ResponsePageDTO response = ratingService.getList(item.getId(),
            RequestPageDTO.builder().page(1).size(5).build());

        //Assert
        assertEquals(response.getPage(),1);
        assertEquals(response.getSize(),5);
        assertEquals(response.getTotalPage(),4);
        assertEquals(response.isHasNext(),true);
        RatingResponse ratingResponse = (RatingResponse) response.getContents().get(0);
        assertEquals(ratingResponse.getContent(),"좋아요20");
        assertEquals(ratingResponse.getScore(),1);
        assertEquals(ratingResponse.getNickname(),"유저2");
        assertEquals(ratingResponse.getItemId(),item.getId());
    }

    @Test
    @DisplayName("리뷰 수정")
    public void test5(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-img02");
        memberRepository.save(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        Rating rating = createRating(anotherMember, item, 10, "내용");
        ratingRepository.save(rating);

        //Act
        ratingService.edit(item.getId(),rating.getId(),RatingEdit.builder().content("좋아요").build(),
            createUserSession(anotherMember));

        //Assert
        Rating findRating = ratingRepository.findById(rating.getId()).get();
        assertEquals(findRating.getContent(),"좋아요");
        assertEquals(findRating.getScore(),10);
    }

    @Test
    @DisplayName("리뷰 수정은 리뷰 작성자만 할 수 있습니다.")
    public void test6(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-img02");
        memberRepository.save(anotherMember);
        Member thirdMember = createMember("user3", "password3", "유저3", "STUDENT", "member-img03");
        memberRepository.save(thirdMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        Rating rating = createRating(anotherMember, item, 10, "내용");
        ratingRepository.save(rating);

        //Act
        assertThrows(ForbiddenException.class,()->{ratingService.edit(item.getId(),rating.getId(),RatingEdit.builder().content("좋아요").build(),
            createUserSession(thirdMember));});

    }

    @Test
    @DisplayName("리뷰 수정 or 삭제시 리뷰가 정확히 어떤 아이템의 리뷰인지 URL 경로에 정확하게 표시해야 합니다.(만약 리뷰와 연관된 아이템 != URL 경로상 아이템  -> 수정 실패)")
    public void test6_1(){ // 도메인 유의성이 있어 테스트
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-img02");
        memberRepository.save(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        Item anotherItem = createItem("아이템2", 20000, "item-img03", "item-img04", member);
        itemRepository.save(anotherItem);
        Rating rating = createRating(anotherMember, item, 10, "내용");
        ratingRepository.save(rating);

        //Act
        assertThrows(RatingNotFound.class,()->{ratingService.edit(anotherItem.getId(),rating.getId(),RatingEdit.builder().content("좋아요").build(),
            createUserSession(anotherMember));});

    }

    @Test
    @DisplayName("리뷰 삭제")
    public void test7(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-img02");
        memberRepository.save(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        Rating rating = createRating(anotherMember, item, 10, "내용");
        ratingRepository.save(rating);

        //Act
        ratingService.delete(item.getId(),rating.getId(), createUserSession(anotherMember));

        //Assert
        Rating findRating = ratingRepository.findById(rating.getId()).get();
        assertEquals(findRating.isDeleted(),true);
    }

    @Test
    @DisplayName("리뷰 삭제는 리뷰 작성자만 할 수 있습니다.")
    public void test8(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-img02");
        memberRepository.save(anotherMember);
        Member thirdMember = createMember("user3", "password3", "유저3", "STUDENT", "member-img03");
        memberRepository.save(thirdMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        Rating rating = createRating(anotherMember, item, 10, "내용");
        ratingRepository.save(rating);

        //Act
        assertThrows(ForbiddenException.class,()->{ratingService.delete(item.getId(),rating.getId(), createUserSession(thirdMember));});

    }

//    @Test
//    @DisplayName("리뷰 삭제시 리뷰가 정확히 어떤 아이템의 리뷰인지 URL 경로에 정확하게 표시해야 합니다.(만약 리뷰와 연관된 아이템 != URL 경로상 아이템  -> 삭제 실패)")
//    public void test8_1(){ // 도메인 유의성이 있어 테스트
//        //Arrange
//        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
//        memberRepository.save(member);
//        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-img02");
//        memberRepository.save(anotherMember);
//        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
//        itemRepository.save(item);
//        Item anotherItem = createItem("아이템2", 20000, "item-img03", "item-img04", member);
//        itemRepository.save(anotherItem);
//        Rating rating = createRating(anotherMember, item, 10, "내용");
//        ratingRepository.save(rating);
//
//        //Act
//        assertThrows(BadRequestException.class,()->{ratingService.delete(anotherItem.getId(),rating.getId(), createUserSession(anotherMember));});
//
//    }
    /**
     * 이미 삭제된 리뷰를 삭제 Or 수정? -> 예외발생-> 테스트 가치가 떨어짐.
     * 애시당초 없는 리뷰를 삭제 Or 수정 -> 예외발생 -> 테스트 가치가 떨어짐.
     */


    private Member createMember(String loginId, String password, String name, String group,
        String imgName) {
        return Member.createMember(
            MemberRequest.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .group(group)
                .imageDTO(new ImageDTO(imgName))
                .build());
    }

    private Item createItem(String itemName,int price,String imgName1,String imgName2,Member member){
        ItemRequest itemRequest = new ItemRequest(itemName, price);
        itemRequest.getImageDTOList().add(new ImageDTO(imgName1));
        itemRequest.getImageDTOList().add(new ImageDTO(imgName2));
        return Item.createItem(itemRequest,member);
    }

    private Rating createRating(Member loginMember,Item item,Integer score,String content){
        return Rating.createRating(loginMember,item,new RatingRequest(score,content));
    }

    private UserSession createUserSession(Member member){
        return new UserSession(member.getId());
    }
}