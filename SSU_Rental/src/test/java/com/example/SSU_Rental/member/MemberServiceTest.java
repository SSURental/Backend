package com.example.SSU_Rental.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardRepository;
import com.example.SSU_Rental.board.BoardRequest;
import com.example.SSU_Rental.board.BoardResponse;
import com.example.SSU_Rental.boardrp.Boardrp;
import com.example.SSU_Rental.boardrp.BoardrpRepository;
import com.example.SSU_Rental.boardrp.BoardrpRequest;
import com.example.SSU_Rental.boardrp.BoardrpResponse;
import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.BadRequestException;
import com.example.SSU_Rental.exception.ForbiddenException;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.item.ItemRepository;
import com.example.SSU_Rental.item.ItemRequest;
import com.example.SSU_Rental.item.ItemResponse;
import com.example.SSU_Rental.item.ItemStatus;
import com.example.SSU_Rental.login.UserSession;
import com.example.SSU_Rental.rating.Rating;
import com.example.SSU_Rental.rating.RatingRepository;
import com.example.SSU_Rental.rating.RatingRequest;
import com.example.SSU_Rental.rating.RatingResponse;
import com.example.SSU_Rental.rental.Rental;
import com.example.SSU_Rental.rental.RentalRepository;
import com.example.SSU_Rental.rental.RentalResponse;
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
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardrpRepository boardrpRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @BeforeEach
    public void clear(){
        rentalRepository.deleteAll();
        ratingRepository.deleteAll();
        boardrpRepository.deleteAll();
        boardRepository.deleteAll();
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입")
    public void test1(){
        //Arrange
        MemberRequest memberRequest = MemberRequest.builder().loginId("user1").password("password1")
            .name("유저1")
            .group("STUDENT").imageDTO(new ImageDTO("member-img01")).build();

        //Act
        Long memberId = memberService.register(memberRequest);

        //Arrange
        Member member = memberRepository.getMember(memberId);
        Assertions.assertEquals(member.getLoginId(),"user1");
        Assertions.assertEquals(member.getPassword(),"password1");
        Assertions.assertEquals(member.getName(),"유저1");
        Assertions.assertEquals(member.getMemberGroup(), Group.STUDENT);
        Assertions.assertEquals(member.getMemberImage().getImgName(),"member-img01");
//         LazyInitializationException 예외로 인해 멤버이미지 이름을 확인하기 불가능 -> 방법을 찾아야..
//      특이점) 만약 멤버 이미지의 아이디는 조회 가능(아마도 멤버이미지 아이디는 멤버에 들어있기 때문인듯)

    }

    @Test
    @DisplayName("회원 가입 실패 (이유:로그인 아이디가 같으면 안된다.)")
    public void test1_1(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "img01", "img02");
        memberRepository.save(member);
        MemberRequest memberRequest = MemberRequest.builder().loginId("user1").password("password2")
            .name("유저2")
            .group("STUDENT").imageDTO(new ImageDTO("member-img01")).build();

        //Act
        assertThrows(BadRequestException.class,()->{memberService.register(memberRequest);});


    }



    @Test
    @DisplayName("회원 1명 조회")
    public void test2(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);

        //Act
        MemberResponse response = memberService.getOne(member.getId());

        //Arrange
        assertEquals(response.getId(),member.getId());
        assertEquals(response.getLoginId(),"user1");
        assertEquals(response.getGroup(),"STUDENT");
        assertEquals(response.getName(),"유저1");
        assertEquals(response.getImageDTO().getImgName(),"member-img01");

    }

    /**
     * Test3은 테스트 가치가 있는지 잘 모르겠다.(너무 로직이 단순함)
     */
//    @Test
//    @DisplayName("잘못된 회원 아이디(혹은 삭제된 회원 아이디)로 멤버를 조회할 수 없습니다.")
//    public void test3(){
//        //Arrange
//        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
//        memberRepository.save(member);
//
//        //Act
//        assertThrows(NullPointerException.class,()->{memberService.getOne(member.getId()+2L);});
//
//    }


    @Test
    @DisplayName("회원 수정")
    public void test4(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);

        //Act
        memberService.edit(member.getId(),new MemberEdit("유저2",new ImageDTO("")),createUserSession(member));

        //Assert
        Member findMember = memberRepository.getMember(member.getId());
        assertEquals(findMember.getName(),"유저2");
        assertEquals(findMember.getMemberImage().getImgName(),"member-img01");

    }

    @Test
    @DisplayName("회원 수정 실패(이유: 나의 회원 수정이 아닌 다른 사람의 회원 정보를 수정하려함)")
    public void test5(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-img02");
        memberRepository.save(anotherMember);

        //Act
        assertThrows(ForbiddenException.class,()->{memberService.edit(member.getId(),new MemberEdit("유저2",new ImageDTO("")),createUserSession(anotherMember));});
    }


    @Test
    @DisplayName("특정 회원이 작성한 게시글 페이지 목록 보기")
    public void test6(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        List<Board> boards = IntStream.rangeClosed(1, 20)
            .mapToObj(i -> createBoard("제목" + i, "내용" + i,member)).collect(
                Collectors.toList());
        boardRepository.saveAll(boards);

        //Act
        ResponsePageDTO responsePage = memberService.getMyBoardList(
            RequestPageDTO.builder().page(1).size(5).build(), createUserSession(member));

        //Assert
        assertEquals(responsePage.getPage(),1);
        assertEquals(responsePage.getSize(),5);
        assertEquals(responsePage.getTotalPage(),4);
        assertEquals(responsePage.isHasNext(),true);
        BoardResponse boardResponse = (BoardResponse) responsePage.getContents().get(0);
        assertEquals(boardResponse.getNickname(),"유저1");
        assertEquals(boardResponse.getTitle(),"제목20");
        assertEquals(boardResponse.getContent(),"내용20");

    }

    @Test
    @DisplayName("특정 회원이 작성한 댓글 페이지 목록 보기")
    public void test7(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        Board board = createBoard("제목", "내용", member);
        boardRepository.save(board);
        List<Boardrp> boardrps = IntStream.rangeClosed(1, 20)
            .mapToObj(i -> createBoardrp(board, member, "내용" + i)).collect(Collectors.toList());
        boardrpRepository.saveAll(boardrps);


        //Act
        ResponsePageDTO responsePage = memberService.getMyReplyList(
            RequestPageDTO.builder().page(1).size(5).build(), createUserSession(member));

        //Assert
        assertEquals(responsePage.getPage(),1);
        assertEquals(responsePage.getSize(),5);
        assertEquals(responsePage.getTotalPage(),4);
        assertEquals(responsePage.isHasNext(),true);
        BoardrpResponse boardrpResponse = (BoardrpResponse) responsePage.getContents().get(0);
        assertEquals(boardrpResponse.getNickname(),"유저1");
        assertEquals(boardrpResponse.getBoardId(),board.getId());
        assertEquals(boardrpResponse.getContent(),"내용20");

    }

    @Test
    @DisplayName("특정 회원이 가지고 있는 아이템 페이지 목록으로 보기")
    public void test8(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        List<Item> items = IntStream.rangeClosed(1, 20)
            .mapToObj(i -> createItem("아이템" + i, i * 1000, "img" + i, "image" + i, member)).collect(
                Collectors.toList());
        itemRepository.saveAll(items);


        //Act
        ResponsePageDTO responsePage = memberService.getMyItemList(
            RequestPageDTO.builder().page(1).size(5).build(), createUserSession(member));

        //Assert
        assertEquals(responsePage.getPage(),1);
        assertEquals(responsePage.getSize(),5);
        assertEquals(responsePage.getTotalPage(),4);
        assertEquals(responsePage.isHasNext(),true);
        ItemResponse itemResponse = (ItemResponse) responsePage.getContents().get(0);
        assertEquals(itemResponse.getItemName(),"아이템20");
        assertEquals(itemResponse.getPrice(),20000);
        assertEquals(itemResponse.getMemberName(),"유저1");
        assertEquals(itemResponse.getStatus(), ItemStatus.AVAILABLE);
        assertEquals(itemResponse.getImageDTOList().get(0).getImgName(), "img20");
        assertEquals(itemResponse.getImageDTOList().get(1).getImgName(), "image20");
    }

    @Test
    @DisplayName("특정 회원이 작성한 리뷰 페이지 목록 보기")
    public void test9(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-img02");
        memberRepository.save(anotherMember);
        Item item = createItem("아이템", 10000, "img01", "img02", member);
        itemRepository.save(item);
        List<Rating> ratings = IntStream.rangeClosed(1, 20).mapToObj(i -> {
            return createRating(anotherMember, item, (i % 10) + 1, "좋아요" + i);
        }).collect(Collectors.toList());
        ratingRepository.saveAll(ratings);

        //Act
        ResponsePageDTO responsePage = memberService.getMyRatingList(
            RequestPageDTO.builder().page(1).size(5).build(), createUserSession(anotherMember));

        //Assert
        assertEquals(responsePage.getPage(),1);
        assertEquals(responsePage.getSize(),5);
        assertEquals(responsePage.getTotalPage(),4);
        assertEquals(responsePage.isHasNext(),true);
        RatingResponse ratingResponse = (RatingResponse) responsePage.getContents().get(0);
        assertEquals(ratingResponse.getContent(),"좋아요20");
        assertEquals(ratingResponse.getNickname(),"유저2");
        assertEquals(ratingResponse.getScore(),1);
        assertEquals(ratingResponse.getItemId(),item.getId());

    }

    @Test
    @DisplayName("특정 회원이 렌탈한 목록보기")
    public void test10(){
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "member-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "member-img02");
        memberRepository.save(anotherMember);
        List<Item> items = IntStream.rangeClosed(1, 20)
            .mapToObj(i -> createItem("아이템" + i, i * 1000, "img" + i, "image" + i, member)).collect(
                Collectors.toList());
        itemRepository.saveAll(items);

        List<Rental> rentals = new ArrayList<>();
        for (Item item : items) {
            rentals.add(createRental(anotherMember,item));
        }
        rentalRepository.saveAll(rentals);
        itemRepository.saveAll(items);


        //Act
        ResponsePageDTO responsePage = memberService.getMyRentalList(
            RequestPageDTO.builder().page(1).size(5).build(), createUserSession(anotherMember));

        //Assert
        assertEquals(responsePage.getPage(),1);
        assertEquals(responsePage.getSize(),5);
        assertEquals(responsePage.getTotalPage(),4);
        assertEquals(responsePage.isHasNext(),true);
        RentalResponse rentalResponse = (RentalResponse) responsePage.getContents().get(0);
        assertEquals(rentalResponse.getItemName(),"아이템20");
        assertEquals(rentalResponse.getNickname(),"유저2");
        assertEquals(rentalResponse.getStartDate(), LocalDate.now());
        assertEquals(rentalResponse.getEndDate(), LocalDate.now().plusDays(7));
        assertEquals(rentalResponse.getImageDTO().getImgName(), "img20");
    }


    /**
     * 애시당초 없는 회원을 삭제 Or 수정 -> 예외발생 -> 테스트 가치가 떨어짐.
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

    private Board createBoard(String title, String content, Member member) {
        return Board.createBoard(new BoardRequest(title, content), member);
    }

    private Boardrp createBoardrp(Board board,Member member,String content){
        return Boardrp.createBoardrp(board, member, new BoardrpRequest(content));
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

    private Rental createRental(Member loginMember, Item item){
        return item.rental(loginMember);
    }

    private UserSession createUserSession(Member member){
        return new UserSession(member.getId());
    }
}